package rzk.wirelessredstone.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.network.PacketSetFrequency;
import rzk.wirelessredstone.rsnetwork.Device;
import rzk.wirelessredstone.util.LangKeys;

public class ScreenFrequency extends Screen
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(WirelessRedstone.MOD_ID, "textures/gui/frequency.png");
	public static final ResourceLocation TEXTURE_EXTENDED = new ResourceLocation(WirelessRedstone.MOD_ID, "textures/gui/frequency_extended.png");
	private BlockPos pos;
	private Hand hand;
	private int xSize;
	private int ySize;
	private int guiLeft;
	private int guiTop;
	private short frequency;

	// Standard GUI

	private SizedButton sub1Button;
	private SizedButton sub10Button;
	private SizedButton add1Button;
	private SizedButton add10Button;
	private SizedButton extendButton;
	private SizedButton closeButton;
	private SizedButton doneButton;
	private TextFieldWidget frequencyField;

	// Extended GUI (WIP)
	private boolean extended;
	private Button buttonExtend;
	private TextFieldWidget frequencyName;
	private Button buttonAddName;
	private TextFieldWidget searchbar;

	public ScreenFrequency(Device device)
	{
		super(new TranslationTextComponent(LangKeys.GUI_FREQUENCY));
		this.frequency = device.getFrequency();
		pos = device.isBlock() ? ((Device.Block) device).getFreqPos() : null;
		hand = device.isRemote() ? ((Device.Item) device).getHand() : null;
		extended = false;
		ySize = extended ? 112 : 176;
	}

	@Override
	protected void init()
	{
		xSize = 192;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		// Standard GUI

		addButton(closeButton = new SizedButton(guiLeft + xSize - 18, guiTop + 6, 12, 12, new StringTextComponent("x"), 0, -1, button -> onClose()));
		addButton(sub1Button = new SizedButton(guiLeft + 28, guiTop + 24, 36, 16, new StringTextComponent(hasShiftDown() ? "-100" : "-1"), onPress -> freqButtonPressed(-1)));
		addButton(sub10Button = new SizedButton(guiLeft + 28, guiTop + 44, 36, 16, new StringTextComponent(hasShiftDown() ? "-1000" : "-10"), onPress -> freqButtonPressed(-10)));
		addButton(add1Button = new SizedButton(guiLeft + 128, guiTop + 24, 36, 16, new StringTextComponent(hasShiftDown() ? "+100" : "+1"), onPress -> freqButtonPressed(1)));
		addButton(add10Button = new SizedButton(guiLeft + 128, guiTop + 44, 36, 16, new StringTextComponent(hasShiftDown() ? "+1000" : "+10"), onPress -> freqButtonPressed(10)));
		addButton(doneButton = new SizedButton(guiLeft + 78, guiTop + 64, 36, 18, new TranslationTextComponent(LangKeys.GUI_DONE), onPress -> sendPacket()));

		frequencyField = new TextFieldWidget(font, guiLeft + 76, guiTop + 35, 38, 14, new TranslationTextComponent(LangKeys.GUI_FREQUENCY))
		{
			@Override
			public void insertText(String textToWrite)
			{
				StringBuilder stringbuilder = new StringBuilder();

				for (char c0 : textToWrite.toCharArray())
					if (c0 >= 48 && c0 <= 57)
						stringbuilder.append(c0);

				super.insertText(stringbuilder.toString());
			}
		};

		frequencyField.setMaxLength(5);
		setFrequency(frequency);
		frequencyField.setResponder(text ->
		{
			if (text.trim().isEmpty())
			{
				frequency = 0;
				doneButton.active = false;
			}
			else
			{
				doneButton.active = true;
				int freq = Integer.parseInt(text);
				if (freq <= 0xffff)
					frequency = (short) freq;
				else
					doneButton.active = false;
			}
		});
		children.add(frequencyField);

		// Extended GUI

		addButton(buttonExtend = new SizedButton(guiLeft + xSize - 48, guiTop + ySize - 22, 42, 16, new TranslationTextComponent(LangKeys.GUI_EXTEND), this::extend));
		buttonExtend.active = false;
		buttonExtend.visible = false;
		frequencyName = new TextFieldWidget(font, guiLeft + 7, guiTop + 100, 144, 14, new TranslationTextComponent(LangKeys.GUI_FREQUENCY));
		addButton(buttonAddName = new SizedButton(guiLeft + 154, guiTop + 99, 32, 16, new TranslationTextComponent(LangKeys.GUI_DONE), onPress -> System.out.println("add to list")));
		buttonAddName.visible = extended;
		searchbar = new TextFieldWidget(font, guiLeft + 7, guiTop + 130, 178, 14, new TranslationTextComponent(LangKeys.GUI_REDUCE));

		children.add(frequencyName);
		children.add(searchbar);
	}

	private void setFrequency(int frequency)
	{
		this.frequency = (short) Math.min(frequency, 0xffff);
		frequencyField.setValue(String.valueOf(Short.toUnsignedInt(this.frequency)));
	}

	private void freqButtonPressed(int value)
	{
		short frequencyVal = (short) (hasShiftDown() ? value * 100 : value);
		setFrequency(frequency + frequencyVal);
	}

	private void extend(Button button)
	{
		extended = !extended;

		if (extended)
		{
			ySize = 176;
			buttonExtend.setMessage(new TranslationTextComponent(LangKeys.GUI_REDUCE));
		}
		else
		{
			ySize = 112;
			buttonExtend.setMessage(new TranslationTextComponent(LangKeys.GUI_EXTEND));
		}

		buttonAddName.visible = extended;
		init();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		if (super.keyPressed(keyCode, scanCode, modifiers))
			return true;

		InputMappings.Input key = InputMappings.getKey(keyCode, scanCode);

		if (minecraft.options.keyInventory.isActiveAndMatches(key))
		{
			onClose();
			return true;
		}

		if (hasShiftDown())
		{
			sub1Button.setMessage(new StringTextComponent("-100"));
			sub10Button.setMessage(new StringTextComponent("-1000"));
			add1Button.setMessage(new StringTextComponent("+100"));
			add10Button.setMessage(new StringTextComponent("+1000"));
			return true;
		}

		return false;
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers)
	{
		if (super.keyReleased(keyCode, scanCode, modifiers))
			return true;

		if (!hasShiftDown())
		{
			sub1Button.setMessage(new StringTextComponent("-1"));
			sub10Button.setMessage(new StringTextComponent("-10"));
			add1Button.setMessage(new StringTextComponent("+1"));
			add10Button.setMessage(new StringTextComponent("+10"));
			return true;
		}

		return false;
	}

	@Override
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(matrixStack);
		drawGuiBackgroundTexture(matrixStack, mouseX, mouseY, partialTicks);

		font.draw(matrixStack, title.getString(), guiLeft + (xSize - font.width(title.getString())) / 2, guiTop + 7, 0x404040);
		frequencyField.render(matrixStack, mouseX, mouseY, partialTicks);

		if (extended)
		{
			font.draw(matrixStack, new TranslationTextComponent(LangKeys.GUI_FREQUENCY).getString(), guiLeft + 6, guiTop + 80, 0x404040);
			frequencyName.render(matrixStack, mouseX, mouseY, partialTicks);
			searchbar.render(matrixStack, mouseX, mouseY, partialTicks);
		}

		super.render(matrixStack, mouseX, mouseY, partialTicks);
	}

	private void drawGuiBackgroundTexture(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
	{
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		minecraft.getTextureManager().bind(extended ? TEXTURE_EXTENDED : TEXTURE);
		blit(matrixStack, guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	private void sendPacket()
	{
		if (pos != null)
			PacketHandler.instance.sendToServer(new PacketSetFrequency(frequency, extended, pos));
		else if (hand != null)
			PacketHandler.instance.sendToServer(new PacketSetFrequency(frequency, extended, hand));

		onClose();
	}

	@Override
	public boolean isPauseScreen()
	{
		return false;
	}
}
