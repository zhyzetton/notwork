package com.notwork.notwork_backend.entity.vo;

import reactor.core.publisher.Flux;

import java.util.List;

public record ChatRagResult(Flux<String> stream, List<BlogReferenceVo> references) {
}
