package org.pozi.photonium.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PhotoniumLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger("Photonium Optimizer");

    private static int sleepingItems = 0;
    private static int frozenEntities = 0;
    private static int mergedXpOrbs = 0;

    private static int timer = 0;

    public static void logItemSleep() { sleepingItems++; }
    public static void logEntityFreeze() { frozenEntities++; }
    public static void logXpMerge() { mergedXpOrbs++; }

    public static void tick() {
        timer++;

        if (timer >= 600) {
            printReport();
            reset();
        }
    }

    private static void printReport() {
        if (sleepingItems == 0 && frozenEntities == 0 && mergedXpOrbs == 0) {
            return;
        }

        StringBuilder report = new StringBuilder();
        report.append("Optimization Report (Last 30s): ");

        boolean added = false;

        if (sleepingItems > 0) {
            report.append(sleepingItems).append(" Items slept");
            added = true;
        }

        if (frozenEntities > 0) {
            if (added) report.append(", ");
            report.append(frozenEntities).append(" AI frozen");
            added = true;
        }

        if (mergedXpOrbs > 0) {
            if (added) report.append(", ");
            report.append(mergedXpOrbs).append(" XP orbs merged");
        }

        LOGGER.info(report.toString());
    }

    private static void reset() {
        sleepingItems = 0;
        frozenEntities = 0;
        mergedXpOrbs = 0;
        timer = 0;
    }
}
