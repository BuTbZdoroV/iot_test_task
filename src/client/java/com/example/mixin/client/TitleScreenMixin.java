// TitleScreenMixin.java
package com.example.mixin.client;

import com.example.render.screen.CustomScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin extends Screen {

    protected TitleScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addCustomButton(CallbackInfo ci) {
        // Вычисляем позицию для новой кнопки (например, под кнопкой "Singleplayer")
        int y = this.height / 4 + 48 + 72 + 20;
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Сосал?"), button -> {
            this.client.setScreen(new CustomScreen(new TitleScreen()));
        }).dimensions(this.width / 2 - 100, y, 200, 20).build());
    }
}