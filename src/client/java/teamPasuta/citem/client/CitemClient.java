package teamPasuta.citem.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;

import java.util.Random;

public class CitemClient implements ClientModInitializer {

    private static final String KEY_CATEGORY_CITEM = "category.citem";
    private static final String KEY_SPECIAL_ACTION = "key.citem.special_action";
    private static KeyBinding specialActionKey;

    public static int animationTicks = 0;
    public static final int animationDuration = 20; // Animation duration in ticks (1 second)
    public static int activeAnimationId = 0;
    private static final Random random = new Random();

    @Override
    public void onInitializeClient() {
        specialActionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SPECIAL_ACTION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                KEY_CATEGORY_CITEM
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            // Handle key press to start animation
            while (specialActionKey.wasPressed()) {
                if (client.player != null && animationTicks == 0) { // Only start if not already animating
                    ItemStack mainHandStack = client.player.getMainHandStack();
                    if (!mainHandStack.isEmpty() && (mainHandStack.getItem() instanceof ToolItem || mainHandStack.getItem() instanceof BlockItem)) {
                        // Choose a random animation
                        activeAnimationId = random.nextInt(3); // Generates a random number between 0 and 2
                        animationTicks = animationDuration;
                    }
                }
            }

            // Decrement animation timer
            if (animationTicks > 0) {
                animationTicks--;
            }
        });
    }
}