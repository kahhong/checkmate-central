package com.ila.checkmatecentral.utility;


import lombok.experimental.UtilityClass;

@UtilityClass
public class GlickoCalculator {
    private static final double Q = Math.log(10) / 400;
    private static final double RD_CONSTANT = 50;
    private static final double MAX_RD = 350;


    public static double calculateNewRating(double rating, double ratingDeviation, double opponentRating, double opponentRatingDeviation, double score) {
        double gFactor = calculate_gFactor(opponentRatingDeviation);
        double e = calculateE(rating, opponentRating, gFactor);
        double dSquared = calculateDSquared(gFactor, e);

        return rating + (Q / (1 / (ratingDeviation * ratingDeviation) + 1 / dSquared)) * gFactor * (score - e);
    }

    public static double calculateNewRD(double rating, double ratingDeviation, double opponentRating, double opponentRatingDeviation, double score) {
        double gFactor = calculate_gFactor(opponentRatingDeviation);
        double e = calculateE(rating, opponentRating, gFactor);
        double dSquared = calculateDSquared(gFactor, e);
        return Math.sqrt(1 / ((1 / (ratingDeviation * ratingDeviation)) + (1 / dSquared)));
    }


    // gFactor formula = 1 / sqrt(1 + 3q²(RD²/π²))
    // The purpose of gFactor is to take in mind for the integrity of the opponent's rating
    // The less confident we are about an opponent's true ability (higher rating deviation), the less impact that game should have on a player's rating change

    public static double calculate_gFactor(double ratingDeviation) {
        return 1 / Math.sqrt(1 + (3 * Q * Q * ratingDeviation * ratingDeviation) / (Math.PI * Math.PI));
    }

    // E formula =  1 / (1 + 10^(-g(player_rating - oppornent_rating) / 400))
    // The purpose of E is to predicts the outcome of a match between two players - if a player play better than expected (actual > E), their rating increases else decrease;

    public static double calculateE(double rating, double opponentRating, double gFactor) {
        return 1 / (1 + Math.pow(10, -gFactor * (rating - opponentRating) / 400));
    }

    public static double calculateDSquared(double gFactor, double e) {
        return 1 / (Q * Q * gFactor * gFactor * e * (1 - e));
    }

    // the time period in set by us, normally in months.
    public static double adjustRatingDeviationForInactivity(double ratingDeviation, long timePeriods) {
        return Math.min(Math.sqrt(ratingDeviation * ratingDeviation + RD_CONSTANT * RD_CONSTANT * timePeriods), MAX_RD);
    }

    // can be used to show a match winning probability before the actual match.
    public static double calculateWinProbability(double playerRating, double playerRD, double opponentRating, double opponentRD) {
        double gFactor = calculate_gFactor(Math.sqrt(playerRD * playerRD + opponentRD * opponentRD));
        return calculateE(playerRating, opponentRating, gFactor);
    }
}
