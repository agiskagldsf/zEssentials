package fr.maxlego08.essentials.api.placeholders;

import fr.maxlego08.essentials.api.functionnals.ReturnBiConsumer;
import fr.maxlego08.essentials.api.functionnals.ReturnConsumer;
import org.bukkit.entity.Player;

public interface Placeholder {

    void register(String startWith, ReturnBiConsumer<Player, String, String> biConsumer);

    void register(String startWith, ReturnConsumer<Player, String> biConsumer);

    String getPrefix();

    String onRequest(Player player, String params);
}
