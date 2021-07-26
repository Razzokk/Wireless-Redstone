package rzk.wirelessredstone.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import org.lwjgl.glfw.GLFW;
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
	private InteractionHand hand;
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
	private EditBox frequencyField;

	// Extended GUI (WIP)
	private boolean extended;
	private Button buttonExtend;
	private EditBox frequencyName;
	private Button buttonAddName;
	private EditBox searchbar;

	public ScreenFrequency(Device device)
	{
		super(new TranslatableComponent(LangKeys.GUI_FREQUENCY));
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

		addWidget(closeButton = new SizedButton(guiLeft + xSize - 18, guiTop + 6, 12, 12, new TextComponent("x"), 0, -1, onPress -> onClose()));
		addWidget(sub1Button = new SizedButton(guiLeft + 28, guiTop + 24, 36, 16, new TextComponent(hasShiftDown() ? "-100" : "-1"), onPress -> freqButtonPressed(-1)));
		addWidget(sub10Button = new SizedButton(guiLeft + 28, guiTop + 44, 36, 16, new TextComponent(hasShiftDown() ? "-1000" : "-10"), onPress -> freqButtonPressed(-10)));
		addWidget(add1Button = new SizedButton(guiLeft + 128, guiTop + 24, 36, 16, new TextComponent(hasShiftDown() ? "+100" : "+1"), onPress -> freqButtonPressed(1)));
		addWidget(add10Button = new SizedButton(guiLeft + 128, guiTop + 44, 36, 16, new TextComponent(hasShiftDown() ? "+1000" : "+10"), onPress -> freqButtonPressed(10)));
		addWidget(doneButton = new SizedButton(guiLeft + 78, guiTop + 64, 36, 18, new TranslatableComponent(LangKeys.GUI_DONE), onPress -> sendPacket()));

		addWidget(frequencyField = new EditBox(font, guiLeft + 76, guiTop + 35, 38, 14, new TranslatableComponent(LangKeys.GUI_FREQUENCY))
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
		});

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

		// Extended GUI

		addWidget(buttonExtend = new SizedButton(guiLeft + xSize - 48, guiTop + ySize - 22, 42, 16, new TranslatableComponent(LangKeys.GUI_EXTEND), this::extend));
		buttonExtend.active = false;
		buttonExtend.visible = false;
		addWidget(frequencyName = new EditBox(font, guiLeft + 7, guiTop + 100, 144, 14, new TranslatableComponent(LangKeys.GUI_FREQUENCY)));
		addWidget(buttonAddName = new SizedButton(guiLeft + 154, guiTop + 99, 32, 16, new TranslatableComponent(LangKeys.GUI_DONE), onPress -> System.out.println("add to list")));
		buttonAddName.visible = extended;
		addWidget(searchbar = new EditBox(font, guiLeft + 7, guiTop + 130, 178, 14, new TranslatableComponent(LangKeys.GUI_REDUCE)));
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
			buttonExtend.setMessage(new TranslatableComponent(LangKeys.GUI_REDUCE));
		}
		else
		{
			ySize = 112;
			buttonExtend.setMessage(new TranslatableComponent(LangKeys.GUI_EXTEND));
		}

		buttonAddName.visible = extended;
		init();
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers)
	{
		switch (keyCode)
		{
			case GLFW.GLFW_KEY_LEFT_SHIFT:
			case GLFW.GLFW_KEY_RIGHT_SHIFT:
				sub1Button.setMessage(new TextComponent("-100"));
				sub10Button.setMessage(new TextComponent("-1000"));
				add1Button.setMessage(new TextComponent("+100"));
				add10Button.setMessage(new TextComponent("+1000"));
				break;
		}

		return super.keyPressed(keyCode, scanCode, modifiers);
	}

	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers)
	{
		switch (keyCode)
		{
			case GLFW.GLFW_KEY_LEFT_SHIFT:
			case GLFW.GLFW_KEY_RIGHT_SHIFT:
				sub1Button.setMessage(new TextComponent("-1"));
				sub10Button.setMessage(new TextComponent("-10"));
				add1Button.setMessage(new TextComponent("+1"));
				add10Button.setMessage(new TextComponent("+10"));
				break;
		}

		return super.keyReleased(keyCode, scanCode, modifiers);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		renderBackground(poseStack);
		drawGuiBackgroundTexture(poseStack, mouseX, mouseY, partialTicks);

		font.draw(poseStack, title.getString(), guiLeft + (xSize - font.width(title.getString())) / 2, guiTop + 7, 0x404040);
		frequencyField.render(poseStack, mouseX, mouseY, partialTicks);

		if (extended)
		{
			font.draw(poseStack, new TranslatableComponent(LangKeys.GUI_FREQUENCY).getString(), guiLeft + 6, guiTop + 80, 0x404040);
			frequencyName.render(poseStack, mouseX, mouseY, partialTicks);
			searchbar.render(poseStack, mouseX, mouseY, partialTicks);
		}

		super.render(poseStack, mouseX, mouseY, partialTicks);
	}

	private void drawGuiBackgroundTexture(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
	{
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, extended ? TEXTURE_EXTENDED : TEXTURE);
		blit(poseStack, guiLeft, guiTop, 0, 0, xSize, ySize);
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
