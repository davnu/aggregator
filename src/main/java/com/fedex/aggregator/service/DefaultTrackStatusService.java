package com.fedex.aggregator.service;

import com.fedex.aggregator.service.client.BackendServicesClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class DefaultTrackStatusService implements TrackStatusService {

    private final BackendServicesClient backendServicesClient;

    public DefaultTrackStatusService(BackendServicesClient backendServicesClient) {
        this.backendServicesClient = backendServicesClient;
    }

    @Override
    public Map<String, CompletableFuture<Optional<TrackStatusType>>> getTrackStatuses(List<String> trackOrderNumbers) {
        var futureTrackStatuses = new HashMap<String, CompletableFuture<Optional<TrackStatusType>>>();

        for (String trackOrderNumber : trackOrderNumbers) {
            var futureTrackStatus = backendServicesClient.getTrackStatus(trackOrderNumber);
            futureTrackStatuses.put(trackOrderNumber, futureTrackStatus);
        }

        return futureTrackStatuses;
    }
}
