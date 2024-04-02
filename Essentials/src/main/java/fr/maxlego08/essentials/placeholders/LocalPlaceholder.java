package fr.maxlego08.essentials.placeholders;

import fr.maxlego08.essentials.api.EssentialsPlugin;
import fr.maxlego08.essentials.api.functionnals.ReturnBiConsumer;
import fr.maxlego08.essentials.api.functionnals.ReturnConsumer;
import fr.maxlego08.essentials.api.placeholders.Placeholder;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LocalPlaceholder implements Placeholder {

    /**
     * static Singleton instance.
     */
    private static volatile LocalPlaceholder instance;
    private final Pattern pattern = Pattern.compile("[%]([^%]+)[%]");
    private final List<AutoPlaceholder> autoPlaceholders = new ArrayList<>();
    private final EssentialsPlugin plugin;
    private final String prefix = "zessentials";

    public LocalPlaceholder(EssentialsPlugin plugin) {
        this.plugin = plugin;
    }

    public String setPlaceholders(Player player, String placeholder) {

        if (placeholder == null || !placeholder.contains("%")) {
            return placeholder;
        }

        final String realPrefix = this.prefix + "_";

        Matcher matcher = this.pattern.matcher(placeholder);
        while (matcher.find()) {
            String stringPlaceholder = matcher.group(0);
            String regex = matcher.group(1).replace(realPrefix, "");
            String replace = this.onRequest(player, regex);
            if (replace != null) {
                placeholder = placeholder.replace(stringPlaceholder, replace);
            }
        }

        return placeholder;
    }

    public List<String> setPlaceholders(Player player, List<String> lore) {
        return lore == null ? null : lore.stream().map(e -> e = setPlaceholders(player, e)).collect(Collectors.toList());
    }

    @Override
    public String onRequest(Player player, String string) {

        Optional<AutoPlaceholder> optional = this.autoPlaceholders.stream().filter(autoPlaceholder -> autoPlaceholder.startsWith(string)).findFirst();
        if (optional.isPresent()) {

            AutoPlaceholder autoPlaceholder = optional.get();
            String value = string.replace(autoPlaceholder.getStartWith(), "");
            return autoPlaceholder.accept(player, value);
        }

        return null;
    }

    public void register(String startWith, ReturnBiConsumer<Player, String, String> biConsumer) {
        this.autoPlaceholders.add(new AutoPlaceholder(startWith, biConsumer));
    }

    public void register(String startWith, ReturnConsumer<Player, String> biConsumer) {
        this.autoPlaceholders.add(new AutoPlaceholder(startWith, biConsumer));
    }

    public String getPrefix() {
        return prefix;
    }


    public EssentialsPlugin getPlugin() {
        return plugin;
    }
}
