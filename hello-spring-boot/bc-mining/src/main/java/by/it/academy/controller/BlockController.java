package by.it.academy.controller;

import by.it.academy.pojo.Block;
import by.it.academy.service.BlockService;
import by.it.academy.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@RestController
public class BlockController {

    Logger logger;

    {
        try {
            logger = LoggerUtil.startLogging(BlockController.class.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Autowired
    BlockService blockService;

    @GetMapping("/blocks")
    public List<Block> blocks() {
        logger.info("Returning all blocks to server");
        return blockService.findAllBlocks();
    }

    @GetMapping("/blocks/{blockId}")
    public Block blockByBlockId(@PathVariable String blockId) {
        logger.info("Returning block to server - by id: " + blockId);
        return blockService.findBlockById(blockId);
    }

//    @GetMapping("/blocks-by-miner/{miningSessionId}")
//    public ArrayList<Block> blockByMinerId(@PathVariable String minerId) {
//        logger.info("Returning block to server - by miner id: " + minerId);
//        return blockService.findBlockByMinerId(minerId);
//    }
}
