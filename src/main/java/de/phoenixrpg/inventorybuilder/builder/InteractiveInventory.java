package de.phoenixrpg.inventorybuilder.builder;

import com.google.gson.annotations.Expose;
import de.phoenixrpg.inventorybuilder.InventoryBuilder;
import de.phoenixrpg.inventorybuilder.adapter.PhoenixCraftInventory;
import de.phoenixrpg.inventorybuilder.builder.items.action.FillItemAction;
import de.phoenixrpg.inventorybuilder.builder.items.action.ItemAction;
import de.phoenixrpg.inventorybuilder.builder.slot.action.SlotAction;
import de.phoenixrpg.inventorybuilder.inventories.PhoenixInventory;
import de.phoenixrpg.inventorybuilder.builder.slot.ClickAction;
import de.phoenixrpg.inventorybuilder.io.InventoryWriter;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The @Expose annotation is needed for gson
 * Only the attributes with @Expose gets converted to json
 */
public class InteractiveInventory extends PhoenixInventory {

    @Expose
    protected String key;

    @Expose
    protected String title;

    @Expose
    protected InventoryType inventoryType;

    @Expose
    protected int size;

    @Expose
    protected List<ItemAction> itemActions;

    @Expose
    protected HashMap<Integer, HashMap<ClickAction, SlotAction>> slotActions;

    protected Inventory inventory;

    public InteractiveInventory(String key, String title) {
        this.key = key;
        this.title = title;

        this.inventory = Bukkit.createInventory(null, 9, "");

        this.inventoryType = InventoryType.CHEST;
        this.size = 9;

        this.itemActions = new ArrayList<>();
        this.slotActions = new HashMap<>();
    }

    @Override
    public void runInteract(@NotNull InventoryClickEvent event) {
        if(!slotActions.containsKey(event.getSlot())) return;

        HashMap<ClickAction, SlotAction> actions = slotActions.get(event.getSlot());

        SlotAction firstAction = actions.get(ClickAction.byName(event.getClick().name()));
        if(firstAction != null) firstAction.runAction(event);

        SlotAction secondAction = actions.get(ClickAction.ALL);
        if(secondAction != null) secondAction.runAction(event);
    }

    /**
     * Get all available SlotActions
     *
     * <p>Integer is the slot of the inventory,</p>
     * <p>ClickAction is how to activate the slot action,</p>
     * <p>SlotAction is the action that run on the slot by given ClickAction,</p>
     * @return {@code HashMap<Integer, HashMap<ClickAction, SlotAction>>}
     */
    @NotNull
    public HashMap<Integer, HashMap<ClickAction, SlotAction>> getSlotActions() {
        return slotActions;
    }

    /**
     * Add a slot action to given slot and click action
     * <p>Calls {@code save())}</p>
     * @param slot where the action should be added
     * @param action how to call the action
     * @param slotAction that get called
     */
    public void addSlotAction(int slot, @NotNull ClickAction action, @NotNull SlotAction slotAction) {
        if(!this.slotActions.containsKey(slot)) this.slotActions.put(slot, new HashMap<>());
        this.slotActions.get(slot).put(action, slotAction);
        save();
    }

    /**
     * Remove all slot actions on specific slot
     * <p>Calls {@code save())}</p>
     * @param slot where the slot actions should be removed
     */
    public void removeSlotAction(int slot) {
        if(!this.slotActions.containsKey(slot)) return;
        slotActions.get(slot).clear();
        save();
    }

    /**
     * Get all ItemActions as List
     * @return {@code List<ItemAction>}
     */
    @NotNull
    public List<ItemAction> getItemActions() {
        return itemActions;
    }

    /**
     * Set the entire item action list
     * <p>Calls {@code save())}</p>
     * @param itemActions new list of item actions
     */
    public void setItemActions(@NotNull List<ItemAction> itemActions) {
        this.itemActions = itemActions;
        save();
    }

    /**
     * Add a ItemAction to the lists
     * <p>Calls {@code save())}</p>
     * @param itemAction that should be added
     */
    public void addItemAction(@NotNull ItemAction itemAction) {
        if(!this.itemActions.contains(itemAction)) {
            this.itemActions.add(itemAction);
        }
        save();
    }

    /**
     * Remove specific item action
     * <p>Calls {@code save())}</p>
     * @param action that should be removed
     */
    public void removeItemAction(@NotNull ItemAction action) {
        this.itemActions.remove(action);
        save();
    }

    /**
     * Sorts the entire item action list
     *
     * first Fill Item Actions
     * Within the Fill Item Action sort, they get sorted by priority
     * higher priority is sorted as first
     * second Set Item Action
     *
     * <p>Example result:</p>
     * <p>Fill Priority 10</p>
     * <p>Fill Priority 1</p>
     * <p>Set</p>
     * <p>Set</p>
     */
    public void sortItemActions() {
        this.itemActions = itemActions.stream()
                .sorted(Comparator.comparing(itemAction -> itemAction instanceof FillItemAction)
                        .reversed()
                        .thenComparing(itemAction -> itemAction instanceof FillItemAction ? ((FillItemAction) itemAction).getPriority() : 0))
                .collect(Collectors.toList());
    }

    /**
     * Set the title of the inventory
     * <p>Calls {@code save())}</p>
     * @param title the new title of the inventory
     */
    public void setTitle(@NotNull String title) {
        this.title = title;
        save();
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    /**
     * Counts the inventory size up by 9
     * If the size is higher than 54, reset to 9
     * <p>Calls {@code save())}</p>
     * <p>9 - 18 - 27 - 36 - 45 - 54 - 9</p>
     */
    public void addSize() {
        this.size += 9;
        if(size > 54) this.size = 9;
        save();
    }

    /**
     * Saves the object to json file in the plugin folder
     */
    public void save() {
        InventoryWriter inventoryWriter = InventoryBuilder.getInstance().getInventoryWriter();
        inventoryWriter.writeInventory(this);
    }

    public String getKey() {
        return key;
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Set a new inventory type
     * To prevent misbehaviour the item and slot actions get cleared
     * <p>Calls {@code save())}</p>
     * @param inventoryType
     */
    public void setInventoryType(@NotNull InventoryType inventoryType) {
        this.inventoryType = inventoryType;

        this.slotActions = new HashMap<>();
        this.itemActions = new ArrayList<>();
        this.size = 9;

        save();
    }

    @NotNull
    public InventoryType getInventoryType() {
        return inventoryType;
    }

    @NotNull
    @Override
    public Inventory buildInventory() {
        if(this.inventoryType == InventoryType.CHEST) {
            this.inventory = Bukkit.createInventory(this, this.size, this.title);
        } else {
            this.inventory = new PhoenixCraftInventory(this, inventoryType, title);
        }

        sortItemActions();

        for(ItemAction itemAction : this.itemActions) {
            itemAction.runAction(this.inventory);
        }

        return this.inventory;
    }
}
