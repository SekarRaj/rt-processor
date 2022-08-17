package com.ld.processor.controller;

import com.ld.processor.entity.Deal;
import com.ld.processor.entity.DealDTO;
import com.ld.processor.entity.DealResponse;
import com.ld.processor.entity.DealsDTO;
import com.ld.processor.service.DealService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/processor")
@RequiredArgsConstructor
public class DealController {
    private static final Logger logger = LoggerFactory.getLogger(DealController.class);

    private final DealService dealService;

    @GetMapping
    public Flux<Deal> getAll() {
        dealService.getDeals();
        return dealService.getDeals();
    }

    @PostMapping("/create")
    public Mono<Deal> save(@RequestBody DealDTO deal) {
        logger.info(deal.toString());
        dealService.createDeal(deal);
        return dealService.createDeal(deal);
    }

    @PostMapping(value = "/create-list")
    public Flux<Deal> save(@RequestBody DealsDTO deals) {
        logger.info(deals.toString());
        dealService.createDeals(deals);
        return dealService.createDeals(deals);
    }
}
