package com.fedex.aggregator.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface TrackStatusService {
    Map<String, CompletableFuture<Optional<TrackStatusType>>> getTrackStatuses(List<String> trackOrderNumbers);
}
