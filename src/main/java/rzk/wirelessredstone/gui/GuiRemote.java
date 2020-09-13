package rzk.wirelessredstone.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import rzk.lib.mc.gui.widgets.SizedButton;
import rzk.lib.util.MathUtils;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.packet.PacketFrequencyItem;
import rzk.wirelessredstone.packet.PacketHandler;

@OnlyIn(Dist.CLIENT)
public class GuiRemote extends Screen
{
	public static final ResourceLocation GUI_TEXTURE = new ResourceLocation(WirelessRedstone.MODID, "textures/gui/frequency.png");
	int guiLeft;
	int guiTop;
	private int xSize;
	private int ySize;

	private TextFieldWidget frequencyField;
	private Button done;
	private Button buttonSubtract_1;
	private Button buttonSubtract_10;
	private Button buttonAdd_1;
	private Button buttonAdd_10;

	private int frequency;
	private Hand hand;

	public GuiRemote(int frequency, Hand hand)
	{
		super(new TranslationTextComponent("gui.wirelessredstone.frequency.remote"));
		this.frequency = frequency;
		this.hand = hand;
	}

	@Override
	protected void init()
	{
		xSize = 192;
		ySize = 154;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		addButton(buttonSubtract_1 = new SizedButton(guiLeft + 38, guiTop + 24, 36, 16, "-1", this::buttonPressed));
		addButton(buttonSubtract_10 = new SizedButton(guiLeft + 38, guiTop + 44, 36, 16, "-10", this::buttonPressed));
		addButton(buttonAdd_1 = new SizedButton(guiLeft + 118, guiTop + 24, 36, 16, "+1", this::buttonPressed));
		addButton(buttonAdd_10 = new SizedButton(guiLeft + 118, guiTop + 44, 36, 16, "+10", this::buttonPressed));
		addButton(done = new SizedButton(guiLeft + 80, guiTop + 64, 32, 18, I18n.format("gui.done"), onPress -> sendPacket()));

		frequencyField = new TextFieldWidget(font, guiLeft + 77, guiTop + 30, 38, 16, I18n.format("gui.wirelessredstone.frequency"))
		{
			@Override
			public void writeText(String textToWrite)
			{
				StringBuilder stringbuilder = new StringBuilder();

				for (char c0 : textToWrite.toCharArray())
					if (c0 >= 48 && c0 <= 57)
						stringbuilder.append(c0);

				super.writeText(stringbuilder.toString());
			}
		};

		frequencyField.setMaxStringLength(5);
		frequencyField.setText(String.valueOf(frequency));
		frequencyField.setResponder(text ->
		{
			if (text == null || text.isEmpty())
			{
				done.active = false;
				setFrequency(0);
			}
			else
			{
				done.active = true;
				setFrequency(Integer.parseInt(text));
			}
		});
		children.add(frequencyField);
		validateButtons();
	}

	private void setFrequency(int frequency)
	{
		this.frequency = MathUtils.constrain(frequency, 0, 99999);
		validateButtons();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		switch (keyCode)
		{
			case GLFW.GLFW_KEY_LEFT_SHIFT:
			case GLFW.GLFW_KEY_RIGHT_SHIFT:
				buttonSubtract_1.setMessage("-100");
				buttonSubtract_10.setMessage("-1000");
				buttonAdd_1.setMessage("+100");
				buttonAdd_10.setMessage("+1000");
				validateButtons();
				break;
		}

		return frequencyField.keyPressed(keyCode, scanCode, modifiers) || frequencyField.canWrite() || super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers)
	{
		switch (keyCode)
		{
			case GLFW.GLFW_KEY_LEFT_SHIFT:
			case GLFW.GLFW_KEY_RIGHT_SHIFT:
				buttonSubtract_1.setMessage("-1");
				buttonSubtract_10.setMessage("-10");
				buttonAdd_1.setMessage("+1");
				buttonAdd_10.setMessage("+10");
				validateButtons();
				break;
		}

		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		drawGuiBackgroundTexture(mouseX, mouseY, partialTicks);
		frequencyField.render(mouseX, mouseY, partialTicks);
		font.drawString(title.getFormattedText(), guiLeft + (xSize - font.getStringWidth(title.getFormattedText())) / 2, guiTop + 5, 0x404040);
		super.render(mouseX, mouseY, partialTicks);
	}

	private void drawGuiBackgroundTexture(int mouseX, int mouseY, float partialTicks)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(GUI_TEXTURE);
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	void validateButtons()
	{
		if (frequency + Integer.parseInt(buttonSubtract_1.getMessage()) < 0)
			buttonSubtract_1.active = false;
		else if (frequency + Integer.parseInt(buttonAdd_1.getMessage()) > 99999)
			buttonAdd_1.active = false;
		else
			buttonSubtract_1.active = buttonAdd_1.active = true;

		if (frequency + Integer.parseInt(buttonSubtract_10.getMessage()) < 0)
			buttonSubtract_10.active = false;
		else if (frequency + Integer.parseInt(buttonAdd_10.getMessage()) > 99999)
			buttonAdd_10.active = false;
		else
			buttonSubtract_10.active = buttonAdd_10.active = true;
	}

	private void buttonPressed(Button button)
	{
		frequencyField.setText(String.valueOf(frequency + Integer.parseInt(button.getMessage())));
	}

	private void sendPacket()
	{
		PacketHandler.INSTANCE.sendToServer(new PacketFrequencyItem(frequency, hand));
		minecraft.player.closeScreen();
	}
}
