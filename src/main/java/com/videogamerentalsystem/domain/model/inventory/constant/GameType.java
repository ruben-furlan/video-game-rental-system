package com.videogamerentalsystem.domain.model.inventory.constant;

public enum GameType {
    NEW_RELEASE, STANDARD, CLASSIC;

    public boolean isNewRelease() {
        return NEW_RELEASE.equals(this);
    }

    public boolean isStandard() {
        return NEW_RELEASE.equals(this);
    }

    public boolean isClassic() {
        return NEW_RELEASE.equals(this);
    }
}