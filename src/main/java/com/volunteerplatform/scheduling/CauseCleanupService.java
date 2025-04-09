package com.volunteerplatform.scheduling;

import com.volunteerplatform.service.CauseService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CauseCleanupService {
    private static final Logger log = LoggerFactory.getLogger(CauseCleanupService.class);
    private final CauseService causeService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void removeOldCauses() {
        log.info("Starting scheduled cleanup: Removing causes older than 6 months...");
        int deletedCount = causeService.removeCausesOlderThanMonths(6);
        log.info("Cleanup completed. {} old causes removed.", deletedCount);
    }
}
