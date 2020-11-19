package com.jcg.springbatch;

import com.jcg.springbatch.entity.Movie;
import com.jcg.springbatch.entity.MovieGenre;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.WriteFailedException;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Date;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job movieJob(Step movieStep, Step listStep) {
        return jobBuilderFactory.get("movieJob")
                .incrementer(new RunIdIncrementer())
                .flow(listStep)
                .next(movieStep)
                .end()
                .build();
    }

    @Bean
    public Step movieStep() throws MalformedURLException {
        return stepBuilderFactory
                .get("movieStep")
                .listener(movieStepListener())
                .<Movie, MovieGenre>chunk(10)
                .reader(jsonItemReader())
                .processor(movieListItemProcessor())
                .writer(movieGenreWriter())
                .faultTolerant()
                .skipLimit(10)
                .skip(Exception.class)
                .retryLimit(3)
                .retry(Exception.class)
                .listener(chunkExecutionListener())
                .build();
    }

    @Bean
    public JsonItemReader<Movie> jsonItemReader() throws MalformedURLException {
        return new JsonItemReaderBuilder<Movie>()
                .jsonObjectReader(new JacksonJsonObjectReader<>(Movie.class))
                .resource(new UrlResource("https://raw.githubusercontent.com/prust/wikipedia-movie-data/master/movies.json"))
                .name("movieJsonItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<Movie, MovieGenre> movieListItemProcessor() {
        return movie -> new MovieGenre(movie.getTitle(), movie.getGenres().toString());
    }

    @Bean
    public FlatFileItemWriter<MovieGenre> movieGenreWriter() {
        return new FlatFileItemWriterBuilder<MovieGenre>()
                .name("movieGenreWriter")
                .resource(new FileSystemResource("out/movies.csv"))
                .headerCallback(writer -> writer.write("Movie Title,Movie Genres"))
                .delimited()
                .delimiter(",")
                .names(new String[]{"title", "genre"})
                .build();
    }

    @Bean
    public StepExecutionListener movieStepListener() {
        return new StepExecutionListener() {

            @Override
            public void beforeStep(StepExecution stepExecution) {
                stepExecution.getExecutionContext().put("start", new Date().getTime());
                System.out.println("Step name:" + stepExecution.getStepName() + " Started");
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                long elapsed = new Date().getTime() - stepExecution.getExecutionContext().getLong("start");
                System.out.println("Step name:" + stepExecution.getStepName() + " Ended. Running time is " + elapsed + " milliseconds.");
                System.out.println("Read Count:" + stepExecution.getReadCount() +
                        " Write Count:" + stepExecution.getWriteCount());
                return ExitStatus.COMPLETED;
            }
        };
    }

    @Bean
    public ChunkListener chunkExecutionListener() {
        return new ChunkListener() {

            @Override
            public void beforeChunk(ChunkContext context) {
                int count1 = context.getStepContext().getStepExecution().getReadCount();
                int count2 = context.getStepContext().getStepExecution().getWriteCount();
                System.out.println("ItemCount before: " + count1 + " " + count2);
            }

            @Override
            public void afterChunk(ChunkContext context) {
                int count1 = context.getStepContext().getStepExecution().getReadCount();
                int count2 = context.getStepContext().getStepExecution().getWriteCount();
                System.out.println("ItemCount after: " + count1 + " " + count2);
            }

            @Override
            public void afterChunkError(ChunkContext context) {
            }
        };
    }

    @Bean
    public Step listStep() {
        return stepBuilderFactory.get("listStep").tasklet((stepContribution, chunkContext) -> {
            File dir = new File("C:\\work\\springBatch");
            File dirOut = new File(dir, "out");
            dirOut.mkdir();
            File result = new File(dirOut,"movies.csv");
            result.createNewFile();
            Resource directory = new FileSystemResource("out");
            System.out.println(directory.getFile() + " directory is available");
            for (File file : directory.getFile().listFiles()) {
                System.out.println(file.getName() + " is available");
            }
            return RepeatStatus.FINISHED;
        }).build();
    }
}
