package ru.dtrunin.ifmodroid.pokecalc;

/**
 * Created by dmitry.trunin on 04.09.2016.
 */
public final class PokeMath {

    /**
     * http://pokemongo.gamepress.gg/power-up-costs
     */
    public static int getPowerUpStardustCost(int fromLevel) {
        checkLevelBounds(fromLevel);
        if (fromLevel <= 5) {
            return 200;
        }
        if (fromLevel <= 9) {
            return 400;
        }
        if (fromLevel <= 13) {
            return 600;
        }
        if (fromLevel <= 17) {
            return 800;
        }
        if (fromLevel <= 21) {
            return 1000;
        }
        if (fromLevel <= 25) {
            return 1300;
        }
        if (fromLevel <= 29) {
            return 1600;
        }
        if (fromLevel <= 33) {
            return 1900;
        }
        if (fromLevel <= 37) {
            return 2200;
        }
        if (fromLevel <= 41) {
            return 2500;
        }
        if (fromLevel <= 45) {
            return 3000;
        }
        if (fromLevel <= 49) {
            return 3500;
        }
        if (fromLevel <= 53) {
            return 4000;
        }
        if (fromLevel <= 57) {
            return 4500;
        }
        if (fromLevel <= 61) {
            return 5000;
        }
        if (fromLevel <= 65) {
            return 6000;
        }
        if (fromLevel <= 69) {
            return 7000;
        }
        if (fromLevel <= 73) {
            return 8000;
        }
        if (fromLevel <= 77) {
            return 9000;
        }
        return 10000;
    }

    public static boolean isAllowedPowerUpStardustCost(int powerUpStardustCost) {
        switch (powerUpStardustCost) {
            case 200:
            case 400:
            case 600:
            case 800:
            case 1000:
            case 1300:
            case 1600:
            case 1900:
            case 2200:
            case 2500:
            case 3000:
            case 3500:
            case 4000:
            case 4500:
            case 5000:
            case 6000:
            case 7000:
            case 8000:
            case 9000:
            case 10000:
                return true;
            default:
                return false;
        }
    }

    public static int getMinLevelForPowerUpStardustCost(int powerUpStardustCost) {
        switch (powerUpStardustCost) {
            case 200:   return 2;
            case 400:   return 6;
            case 600:   return 10;
            case 800:   return 14;
            case 1000:  return 18;
            case 1300:  return 22;
            case 1600:  return 26;
            case 1900:  return 30;
            case 2200:  return 34;
            case 2500:  return 38;
            case 3000:  return 42;
            case 3500:  return 46;
            case 4000:  return 50;
            case 4500:  return 54;
            case 5000:  return 58;
            case 6000:  return 62;
            case 7000:  return 66;
            case 8000:  return 70;
            case 9000:  return 74;
            case 10000: return 78;
            default:
                throw new IllegalArgumentException("Not allowed power up stardust cost value: "
                        + powerUpStardustCost);
        }
    }

    public static int getMaxLevelForPowerUpStardustCost(int powerUpStardustCost) {
        if (powerUpStardustCost == 10000) {
            return 79;
        } else {
            return getMinLevelForPowerUpStardustCost(powerUpStardustCost) + 3;
        }
    }

    public static int getCp(int level, int stamina, int attack, int defense) {
        final double levelFactor = getLevelFactor(level);
        final double cp = attack * Math.sqrt(defense) * Math.sqrt(stamina) * levelFactor / 10.0;
        return (int) Math.round(Math.floor(cp));
    }

    public static int getHp(int level, int stamina) {
        final double levelFactor = getLevelFactor(level);
        final double hp = stamina * Math.sqrt(levelFactor);
        return (int) Math.round(Math.floor(hp));
    }

    private static double getLevelFactor(int level) {
        checkLevelBounds(level);
        if (level <= 20) {
            return 0.009426125 * level - 0.010016255;
        } else if (level <= 60) {
            return 0.0089219657 * level + 0.0000389325;
        } else {
            return 0.004459461 * level + 0.267817222;
        }
    }

    private static void checkLevelBounds(int level) {
        if (level < 2 || level > 80) {
            throw new IllegalArgumentException("Allowed values of level are "
                    + "from 2 to 79: " + level);
        }
    }

    private PokeMath() {}
}
