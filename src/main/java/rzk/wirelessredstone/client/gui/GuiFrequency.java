package rzk.wirelessredstone.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;
import rzk.lib.mc.gui.widgets.SizedButton;
import rzk.lib.util.MathUtils;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.client.LangKeys;
import rzk.wirelessredstone.packet.PacketFrequency;
import rzk.wirelessredstone.packet.PacketFrequencyBlock;
import rzk.wirelessredstone.packet.PacketFrequencyItem;
import rzk.wirelessredstone.packet.PacketHandler;

@OnlyIn(Dist.CLIENT)
public class GuiFrequency extends Screen
{
	public static final ResourceLocation GUI_TEXTURE_NORMAL = new ResourceLocation(WirelessRedstone.MODID, "textures/gui/frequency.png");
	public static final ResourceLocation GUI_TEXTURE_EXTENDED = new ResourceLocation(WirelessRedstone.MODID, "textures/gui/frequency_extended.png");
	private ResourceLocation gui_texture = GUI_TEXTURE_NORMAL;

	private int guiLeft;
	private int guiTop;
	private int xSize;
	private int ySize;

	private int frequency;
	private PacketFrequency frequencyPacket;

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

		addButton(close = new SizedButton(guiLeft + xSize - 18, guiTop + 6, 12, 12, "x", 0, -1, button -> minecraft.player.closeScreen()));
		addButton(buttonSubtract_1 = new SizedButton(guiLeft + 28, guiTop + 24, 36, 16, "-1", this::buttonPressed));
		addButton(buttonSubtract_10 = new SizedButton(guiLeft + 28, guiTop + 44, 36, 16, "-10", this::buttonPressed));
		addButton(buttonAdd_1 = new SizedButton(guiLeft + 128, guiTop + 24, 36, 16, "+1", this::buttonPressed));
		addButton(buttonAdd_10 = new SizedButton(guiLeft + 128, guiTop + 44, 36, 16, "+10", this::buttonPressed));
		addButton(done = new SizedButton(guiLeft + 78, guiTop + 64, 36, 18, I18n.format(LangKeys.Gui.DONE), onPress -> sendPacket()));

		frequencyField = new TextFieldWidget(font, guiLeft + 76, guiTop + 35, 38, 14, I18n.format(LangKeys.Gui.FREQUENCY))
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

		// Extended GUI

		addButton(buttonExtend = new SizedButton(guiLeft + xSize - 48, guiTop + ySize - 22, 42, 16, I18n.format(LangKeys.Gui.EXTEND), this::extend));
		buttonExtend.active = false;
		buttonExtend.visible = false;
		frequencyName = new TextFieldWidget(font, guiLeft + 7, guiTop + 100, 144, 14, I18n.format(LangKeys.Gui.FREQUENCY_NAME));
		addButton(buttonAddName = new SizedButton(guiLeft + 154, guiTop + 99, 32, 16, I18n.format(LangKeys.Gui.ADD), onPress -> System.out.println("add to list")));
		buttonAddName.visible = extended;
		searchbar = new TextFieldWidget(font, guiLeft + 7, guiTop + 130, 178, 14, I18n.format(LangKeys.Gui.SEARCHBAR));

		children.add(frequencyName);
		children.add(searchbar);
	}

	private void setFrequency(int frequency)
	{
		this.frequency = MathUtils.constrain(frequency, 0, 99999);
	}

	private void buttonPressed(Button button)
	{
		setFrequency(frequency + Integer.parseInt(button.getMessage()));
		frequencyField.setText(String.valueOf(frequency));
	}

	private void extend(Button button)
	{
		extended = !extended;

		if (extended)
		{
			ySize = 176;
			buttonExtend.setMessage(I18n.format(LangKeys.Gui.REDUCE));
			gui_texture = GUI_TEXTURE_EXTENDED;
		}
		else
		{
			ySize = 96;
			buttonExtend.setMessage(I18n.format(LangKeys.Gui.EXTEND));
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
				buttonSubtract_1.setMessage("-100");
				buttonSubtract_10.setMessage("-1000");
				buttonAdd_1.setMessage("+100");
				buttonAdd_10.setMessage("+1000");
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
				break;
		}

		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(int mouseX, int mouseY, float partialTicks)
	{
		renderBackground();
		drawGuiBackgroundTexture(mouseX, mouseY, partialTicks);

		font.drawString(title.getFormattedText(), guiLeft + (xSize - font.getStringWidth(title.getFormattedText())) / 2, guiTop + 7, 0x404040);
		frequencyField.render(mouseX, mouseY, partialTicks);

		if (extended)
		{
			font.drawString(new TranslationTextComponent(LangKeys.Gui.FREQUENCY_NAME).getFormattedText(), guiLeft + 6, guiTop + 80, 0x404040);
			frequencyName.render(mouseX, mouseY, partialTicks);
			searchbar.render(mouseX, mouseY, partialTicks);
		}

		super.render(mouseX, mouseY, partialTicks);
	}

	private void drawGuiBackgroundTexture(int mouseX, int mouseY, float partialTicks)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bindTexture(gui_texture);
		blit(guiLeft, guiTop, 0, 0, xSize, ySize);
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

	public enum GuiType
	{
		NONE,
		ITEM,
		BLOCK;
	}
}
