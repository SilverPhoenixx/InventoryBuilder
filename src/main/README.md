
---

## Developer
* _SilverPhoenix

#### Overview
* Create interactive inventory with an inventory builder
* Add Item Action (Set Item or Fill Items)
* Add Slot Action (Action that happens, when you click specific slots)
* Switch between different InventoryTypes like Chest, Brewing stand, Hopper, Crafting table
* Dynamically generation of json from your inventory

* Showcase on youtube: https://youtu.be/emMceeX9BE0

#### Developer
* Support for custom item actions
* Support for custom slot actions
---


InventoryBuilder is a plugin for creating inventories with interaction 
like open new inventories, close current inventory, add or remove amount of itemstack within the inventory
I hope the user interface and the user experience is acceptable.

## API
### Custom ItemAction
If you want to create a own item action just extend from **"ItemAction"**
    
    de.phoenixrpg.inventorybuilder.builder.items.action.ItemAction

You have to add <b>@Expose</b> to every attribute in\
the class that should be serialized by GsonBuilder

Override <b>runAction</b> for editing the displayed inventory

Example Classes in:

    de.phoenixrpg.inventorybuilder.builder.items.actions

When you done with your custom ItemAction, you have to create a configuration inventory for
the ItemAction.

Example Classes in:

    de.phoenixrpg.inventorybuilder.inventories.item.actions

Dont forget to use, when you save the item action to the current InteractiveInventory:
    
    this.getInteractiveInventory().addItemAction(YOUR_ITEM_ACTION);

If the configuration inventory is ready, you have to register your ItemAction\
at the ItemActionManager

    InventoryBuilder.
    getItemActionManager().
    addItemAction(YOUR_ITEM_ACTION.class, 
    YOUR_ITEM_ACTION_INVENTORY.class,
    "NAME_OF_YOUR_ITEM_ACTION", 
    DISPLAYED_ITEM_STACK);

### Custom SlotAction
If you want to create a own item action just extend from **"SlotAction"**

    de.phoenixrpg.inventorybuilder.builder.slot.action.SlotAction

You have to add <b>@Expose</b> to every attribute in\
the class that should be serialized by GsonBuilder

Override <b>runAction</b> for editing the displayed inventory

Example Classes in:

    de.phoenixrpg.inventorybuilder.builder.slots.actions

When you done with your custom SlotAction, you have to create a configuration inventory for
the SlotAction.

Example Classes in:

    de.phoenixrpg.inventorybuilder.inventories.slot.actions

Dont forget to use, when you save the item action to the current InteractiveInventory:

    this.getInteractiveInventory().addSlotAction(CLICKED_SLOT, 
                                                 this.getClickAction(), 
                                                 YOUR_SLOT_ACTION);

If the configuration inventory is ready, you have to register your ItemAction\
at the ItemActionManager

    InventoryBuilder.
    getSlotActionManager().
    addSlotAction(YOUR_SLOT_ACTION.class, 
    YOUR_SLOT_ACTION_INVENTORY.class,
    "NAME_OF_YOUR_ITEM_ACTION", 
    DISPLAYED_ITEM_STACK);


### Utils

Items [in de.phoenixrpg.inventorybuilder.util.ItemBuilder]:
- ItemBuilder
