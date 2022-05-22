package es.noobcraft.oneblock.api.utils;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

public class WeighList<T extends Weigh> {
    private final List<WeightEntry<T>> entries = Lists.newArrayList();

    private int totalWeigh;
    private boolean randomized = false;

    public List<T>  getEntries() {
        return ImmutableList.copyOf(entries.stream().map(WeightEntry::getElement).collect(Collectors.toList()));
    }

    public boolean add(T element) {
        randomized = false;
        totalWeigh += element.getWeigh();
        return entries.add(new WeightEntry<>(element));
    }

    public void remove(T element) {
        randomized = false;
        totalWeigh -= element.getWeigh();
        entries.remove(new WeightEntry<>(element));
    }

    public T getRandom() {
        randomize();
        double random = Math.random();
        for (int j = 0; j < entries.size() - 1; j++)
            if (entries.get(j + 1).getChance() > random)
                return entries.get(j).getElement();
        return entries.get(entries.size() - 1).getElement();
    }

    private void randomize() {
        if (randomized) return;

        if (entries.size() <= 0 && totalWeigh <= 0) throw new NullPointerException("List mustn't be empty");

        double base = 0;

        for (WeightEntry<T> entry : entries) {
            double chance = base / totalWeigh;
            entry.setChance(chance);
            base += entry.getWeigh();
        }
        randomized = true;
    }


    public static class WeightEntry<T extends Weigh> {
        @Getter private final double weigh;
        @Getter private final T element;
        @Getter @Setter private double chance;

        public WeightEntry(T element) {
            this.element = element;
            this.weigh = element.getWeigh();
            this.chance = 0;
        }
    }
}
