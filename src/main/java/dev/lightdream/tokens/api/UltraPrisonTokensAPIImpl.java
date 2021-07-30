package dev.lightdream.tokens.api;

import dev.lightdream.tokens.managers.TokensManager;
import dev.lightdream.api.enums.ReceiveCause;
import org.bukkit.OfflinePlayer;

public class UltraPrisonTokensAPIImpl implements UltraPrisonTokensAPI {


    private TokensManager manager;

    public UltraPrisonTokensAPIImpl(TokensManager manager) {

        this.manager = manager;
    }

    @Override
    public long getPlayerTokens(OfflinePlayer p) {
        return this.manager.getPlayerTokens(p);
    }

    @Override
    public boolean hasEnough(OfflinePlayer p, long amount) {
		return this.getPlayerTokens(p) >= amount;
    }

    @Override
    public void removeTokens(OfflinePlayer p, long amount) {
        this.manager.removeTokens(p, amount, null);
    }

    @Override
    public void addTokens(OfflinePlayer p, long amount) {
		this.manager.giveTokens(p, amount, null, ReceiveCause.GIVE);
    }
}
