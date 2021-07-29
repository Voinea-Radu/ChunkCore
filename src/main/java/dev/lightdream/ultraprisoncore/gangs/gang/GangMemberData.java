package dev.lightdream.ultraprisoncore.gangs.gang;

import java.util.UUID;

public class GangMemberData {
    private UUID id;
    private String name;
    private int rank;

    public GangMemberData(UUID var1, String var2, int var3) {
        this.id = var1;
        this.name = var2;
        this.rank = var3;
    }

    public UUID getId() {
        return this.id;
    }

    public void setId(UUID var1) {
        this.id = var1;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String var1) {
        this.name = var1;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int var1) {
        this.rank = var1;
    }
}
