package by.it.academy.controller;

import by.it.academy.pojo.Block;
import by.it.academy.service.BlockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class BlockController {

    @Autowired
    BlockService blockService;

    @GetMapping("/blocks")
    public List<Block> blocks() {
        return blockService.findAllBlocks();
    }

    @GetMapping("/blocks/{blockId}")
    public Block blockByBlockId(@PathVariable String blockId) {
        return blockService.findBlockById(blockId);
    }

    @GetMapping("/blocks-by-miner/{minerId}")
    public ArrayList<Block> blockByMinerId(@PathVariable String minerId) {
        return blockService.findBlockByMinerId(minerId);
    }
}
