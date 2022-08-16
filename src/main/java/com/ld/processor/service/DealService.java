package com.ld.processor.service;

import com.ld.processor.entity.Deal;
import com.ld.processor.entity.DealDTO;
import com.ld.processor.entity.DealsDTO;
import com.ld.processor.repository.DealRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {
    private final DealRepository repository;

    public Mono<Deal> createDeal(DealDTO dto) {
        return repository.save(Deal.from(dto));
    }

    public Flux<Deal> createDeals(DealsDTO dto) {
        List<Deal> deals = dto.deals().stream().map(Deal::from).toList();
        return repository.saveAll(Flux.fromIterable(deals));
    }

    public Flux<Deal> getDeals() {
        return repository.findAll();
    }
}
