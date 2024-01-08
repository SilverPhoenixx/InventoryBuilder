package de.phoenixrpg.inventorybuilder.builder.items.action;

import com.google.gson.annotations.Expose;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FillItemAction extends ItemAction {

    @Expose
    private int priority;

    @Expose
    private int from;

    @Expose
    private int to;

    @Expose
    private int step;


    public FillItemAction(ItemStack itemStack, int priority, int from, int to, int step) {
        super(itemStack);
        this.priority = priority;
        this.from = from;
        this.to = to;
        this.step = step;
    }

    @Override
    public void runAction(Inventory inventory) {
        int currentTo = to > inventory.getSize() ? inventory.getSize() : to;
        for(int slot = from - 1; slot < currentTo; slot += step) {
            inventory.setItem(slot, getItemStack().clone());
        }
    }

    public void addFrom(int from) {
        this.from += from;
        if(this.from < 1) this.from = 1;
        if(this.from > 54) this.from = 54;
    }

    public void addPriority(int priority) {
        this.priority += priority;
        if(this.priority < 0) this.priority = 0;
        if(this.priority > 64) this.priority = 64;
    }

    public void addStep(int step) {
        this.step += step;
        if(this.step < 1) this.step = 1;
        if(this.step > 64) this.step = 64;
    }

    public void addTo(int to) {
        this.to += to;
        if(this.to < 1) this.to = 1;
        if(this.to > 54) this.to = 54;
    }

    public int getPriority() {
        return priority;
    }

    public int getFrom() {
        return from;
    }

    public int getStep() {
        return step;
    }

    public int getTo() {
        return to;
    }
}
