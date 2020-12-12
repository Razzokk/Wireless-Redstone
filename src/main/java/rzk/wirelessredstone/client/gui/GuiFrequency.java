package rzk.wirelessredstone.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.DistExecutor;
import org.lwjgl.glfw.GLFW;
import rzk.lib.mc.gui.widgets.SizedButton;
import rzk.lib.mc.util.Utils;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.LangKeys;
import rzk.wirelessredstone.packet.PacketFrequencyBlock;
import rzk.wirelessredstone.packet.PacketFrequencyItem;
import rzk.wirelessredstone.packet.PacketHandler;

public class GuiFrequency extends Screen
{
	public static final ResourceLocation GUI_TEXTURE_NORMAL = new ResourceLocation(WirelessRedstone.MOD_ID, "textures/gui/frequency.png");
	public static final ResourceLocation GUI_TEXTURE_EXTENDED = new ResourceLocation(WirelessRedstone.MOD_ID, "textures/gui/frequency_extended.png");
	private ResourceLocation gui_texture = GUI_TEXTURE_NORMAL;

	private int guiLeft;
	private int guiTop;
	private int xSize;
	private int ySize;

	private int frequency;

	// Standard GUI
	private GuiType guiType = GuiType.NONE;
	private Hand hand;
	private BlockPos pos;
	private Button close;
	private TextFieldWidget frequencyField;
	private Button buttonSubtract_1;
	private Button buttonSubtract_10;
	private Button buttonAdd_1;
	private Button buttonAdd_10;
	private Button done;

	// Extended GUI
	private boolean extended;
	private Button buttonExtend;
	private TextFieldWidget frequencyName;
	private Button buttonAddName;
	private TextFieldWidget searchbar;

	private GuiFrequency(int frequency)
	{
		super(new TranslationTextComponent(LangKeys.Gui.FREQUENCY));
		this.frequency = frequency;
	}

	public GuiFrequency(int frequency, BlockPos pos)
	{
		this(frequency);
		this.pos = pos;
		guiType = GuiType.BLOCK;
	}

	public GuiFrequency(int frequency, Hand hand)
	{
		this(frequency);
		this.hand = hand;
		guiType = GuiType.ITEM;
	}

	@Override
	protected void init()
	{
		xSize = 192;
		ySize = 96;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2 - 40;

		// Standard GUI

		addButton(close = new SizedButton(guiLeft + xSize - 18, guiTop + 6, 12, 12, new StringTextComponent("x"), 0, -1, button -> minecraft.player.closeScreen()));
		addButton(buttonSubtract_1 = new SizedButton(guiLeft + 28, guiTop + 24, 36, 16, new StringTextComponent("-1"), this::buttonPressed));
		addButton(buttonSubtract_10 = new SizedButton(guiLeft + 28, guiTop + 44, 36, 16, new StringTextComponent("-10"), this::buttonPressed));
		addButton(buttonAdd_1 = new SizedButton(guiLeft + 128, guiTop + 24, 36, 16, new StringTextComponent("+1"), this::buttonPressed));
		addButton(buttonAdd_10 = new SizedButton(guiLeft + 128, guiTop + 44, 36, 16, new StringTextComponent("+10"), this::buttonPressed));
		addButton(done = new SizedButton(guiLeft + 78, guiTop + 64, 36, 18, new TranslationTextComponent(LangKeys.Gui.DONE), onPress -> sendPacket()));

		frequencyField = new TextFieldWidget(font, guiLeft + 76, guiTop + 35, 38, 14, new TranslationTextComponent(LangKeys.Gui.FREQUENCY))
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
			boolean textValid = text != null && !text.isEmpty();
			done.active = textValid;
			setFrequency(textValid ?  Integer.parseInt(text) : 0);
		});
		children.add(frequencyField);

		// Extended GUI

		addButton(buttonExtend = new SizedButton(guiLeft + xSize - 48, guiTop + ySize - 22, 42, 16, new TranslationTextComponent(LangKeys.Gui.EXTEND), this::extend));
		buttonExtend.active = false;
		buttonExtend.visible = false;
		frequencyName = new TextFieldWidget(font, guiLeft + 7, guiTop + 100, 144, 14, new TranslationTextComponent(LangKeys.Gui.FREQUENCY_NAME));
		addButton(buttonAddName = new SizedButton(guiLeft + 154, guiTop + 99, 32, 16, new TranslationTextComponent(LangKeys.Gui.ADD), onPress -> System.out.println("add to list")));
		buttonAddName.visible = extended;
		searchbar = new TextFieldWidget(font, guiLeft + 7, guiTop + 130, 178, 14, new TranslationTextComponent(LangKeys.Gui.SEARCHBAR));

		children.add(frequencyName);
		children.add(searchbar);
	}

	private void setFrequency(int frequency)
	{
		this.frequency = Utils.constrain(frequency, 0, 99999);
	}

	private void buttonPressed(Button button)
	{
		setFrequency(frequency + Integer.parseInt(button.getMessage().getString()));
		frequencyField.setText(String.valueOf(frequency));
	}

	private void extend(Button button)
	{
		extended = !extended;

		if (extended)
		{
			ySize = 176;
			buttonExtend.setMessage(new TranslationTextComponent(LangKeys.Gui.REDUCE));
			gui_texture = GUI_TEXTURE_EXTENDED;
		}
		else
		{
			ySize = 96;
			buttonExtend.setMessage(new TranslationTextComponent(LangKeys.Gui.EXTEND));
			gui_texture = GUI_TEXTURE_NORMAL;
		}
		buttonAddName.visible = extended;
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		switch (keyCode)
		{
			case GLFW.GLFW_KEY_LEFT_SHIFT:
			case GLFW.GLFW_KEY_RIGHT_SHIFT:
				buttonSubtract_1.setMessage(new StringTextComponent("-100"));
				buttonSubtract_10.setMessage(new StringTextComponent("-1000"));
				buttonAdd_1.setMessage(new StringTextComponent("+100"));
				buttonAdd_10.setMessage(new StringTextComponent("+1000"));
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
				buttonSubtract_1.setMessage(new StringTextComponent("-1"));
				buttonSubtract_10.setMessage(new StringTextComponent("-10"));
				buttonAdd_1.setMessage(new StringTextComponent("+1"));
				buttonAdd_10.setMessage(new StringTextComponent("+10"));
				break;
		}

		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrixStack);
		drawGuiBackgroundTexture(matrixStack, mouseX, mouseY, partialTicks);

		font.drawString(matrixStack, title.getString(), guiLeft + (xSize - font.getStringWidth(title.getString())) / 2, guiTop + 7, 0x404040);
		frequencyField.render(matrixStack, mouseX, mouseY, partialTicks);

		if (extended)
		{
			font.drawString(matrixStack, new TranslationTextComponent(LangKeys.Gui.FREQUENCY_NAME).getString(), guiLeft + 6, guiTop + 80, 0x404040);
			frequencyName.render(matrixStack, mouseX, mouseY, partialTicks);
			searchbar.render(matrixStack, mouseX, mouseY, partialTicks);
		}

		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	private void drawGuiBackgroundTexture(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(gui_texture);
		blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	private void sendPacket()
	{
		if (guiType == GuiType.BLOCK)
			PacketHandler.INSTANCE.sendToServer(new PacketFrequencyBlock(frequency, pos));
		else if (guiType == GuiType.ITEM)
			PacketHandler.INSTANCE.sendToServer(new PacketFrequencyItem(frequency, hand));
		minecraft.player.closeScreen();
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}

	public static DistExecutor.SafeRunnable openGui(int frequency, BlockPos pos)
	{
		return () -> Minecraft.getInstance().displayGuiScreen(new GuiFrequency(frequency, pos));
	}

	public static DistExecutor.SafeRunnable openGui(int frequency, Hand hand)
	{
		return () -> Minecraft.getInstance().displayGuiScreen(new GuiFrequency(frequency, hand));
	}

	public enum GuiType
	{
		NONE,
		ITEM,
		BLOCK
	}
}
