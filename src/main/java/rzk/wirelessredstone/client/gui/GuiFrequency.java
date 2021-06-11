package rzk.wirelessredstone.client.gui;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiPageButtonList;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import rzk.wirelessredstone.WirelessRedstone;
import rzk.wirelessredstone.network.PacketFrequency;
import rzk.wirelessredstone.network.PacketHandler;
import rzk.wirelessredstone.util.LangKeys;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiFrequency extends GuiScreen implements GuiPageButtonList.GuiResponder
{
	public static final ResourceLocation TEXTURE = new ResourceLocation(WirelessRedstone.MOD_ID, "textures/gui/frequency.png");

	protected int xSize;
	protected int ySize;
	private int guiLeft;
	private int guiTop;

	private GuiSizedButton sub1Button;
	private GuiSizedButton sub10Button;
	private GuiSizedButton add1Button;
	private GuiSizedButton add10Button;
	private GuiSizedButton extendButton;
	private GuiSizedButton closeButton;
	private GuiSizedButton doneButton;
	private GuiTextField frequencyField;

	private short frequency;
	private boolean extended;
	private final BlockPos pos;
	private final EnumHand hand;

	public GuiFrequency(PacketFrequency packet)
	{
		frequency = packet.getFrequency();
		extended = false;
		pos = packet.getPos();
		hand = packet.getHand();
	}

	@Override
	public void initGui()
	{
		xSize = 192;
		ySize = extended ? 200 : 112;
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		buttonList.clear();
		sub1Button = addButton(new GuiSizedButton(0, guiLeft + 26, guiTop + 26, 34, 16, "-1"));
		sub10Button = addButton(new GuiSizedButton(1, guiLeft + 26, guiTop + 46, 34, 16, "-10"));
		add1Button = addButton(new GuiSizedButton(2, guiLeft + xSize - 34 - 26, guiTop + 26, 34, 16, "+1"));
		add10Button = addButton(new GuiSizedButton(3, guiLeft + xSize - 34 - 26, guiTop + 46, 34, 16, "+10"));
		extendButton = addButton(new GuiSizedButton(4, guiLeft + 26, guiTop + 76, 44, 18, I18n.format(extended ? LangKeys.GUI_REDUCE : LangKeys.GUI_EXTEND)));
		extendButton.visible = false;
		doneButton = addButton(new GuiSizedButton(6, guiLeft + xSize / 2 - 20, guiTop + 76, 40, 18, I18n.format(LangKeys.GUI_DONE)));
		closeButton = addButton(new GuiSizedButton(7, guiLeft + xSize - 16, guiTop + 4, 13, 13, "x", 0, -1));

		frequencyField = new GuiTextField(8, fontRenderer, guiLeft + xSize / 2 - 19, guiTop + 36, 38, 16);
		frequencyField.setValidator(str -> str.matches("^[0-9]*"));
		frequencyField.setMaxStringLength(5);
		frequencyField.setGuiResponder(this);
		setFrequency(Short.toUnsignedInt(frequency));
	}

	private void setFrequency(int frequency)
	{
		this.frequency = (short) Math.min(frequency, 0xffff);
		frequencyField.setText(String.valueOf(Short.toUnsignedInt(this.frequency)));
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch (button.id)
		{
			case 0:
				setFrequency(frequency - (isShiftKeyDown() ? 100 : 1));
				break;
			case 1:
				setFrequency(frequency - (isShiftKeyDown() ? 1000 : 10));
				break;
			case 2:
				setFrequency(frequency + (isShiftKeyDown() ? 100 : 1));
				break;
			case 3:
				setFrequency(frequency + (isShiftKeyDown() ? 1000 : 10));
				break;
			case 4:
				extended = !extended;
				extendButton.displayString = I18n.format(extended ? LangKeys.GUI_REDUCE : LangKeys.GUI_EXTEND);
				initGui();
				break;
			case 6:
				if (pos != null)
					PacketHandler.INSTANCE.sendToServer(new PacketFrequency(frequency, extended, pos));
				else if (hand != null)
					PacketHandler.INSTANCE.sendToServer(new PacketFrequency(frequency, extended, hand));
				else
					WirelessRedstone.logger.error("BlockPos and Hand are null, something went wrong");
				mc.displayGuiScreen(null);
				break;
			case 7:
				mc.displayGuiScreen(null);
				break;
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		super.keyTyped(typedChar, keyCode);
		frequencyField.textboxKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		frequencyField.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		drawDefaultBackground();
		drawBackgroundLayer();

		if (isShiftKeyDown())
		{
			sub1Button.displayString = "-100";
			sub10Button.displayString = "-1000";
			add1Button.displayString = "+100";
			add10Button.displayString = "+1000";
		}
		else
		{
			sub1Button.displayString = "-1";
			sub10Button.displayString = "-10";
			add1Button.displayString = "+1";
			add10Button.displayString = "+10";
		}

		super.drawScreen(mouseX, mouseY, partialTicks);
		drawForegroundLayer();
	}

	protected void drawBackgroundLayer()
	{
		GlStateManager.color(1f, 1f, 1f, 1f);
		mc.getTextureManager().bindTexture(TEXTURE);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
	}

	protected void drawForegroundLayer()
	{
		frequencyField.drawTextBox();
		String title = I18n.format(LangKeys.GUI_FREQUENCY);
		fontRenderer.drawString(title, guiLeft + xSize / 2 - fontRenderer.getStringWidth(title) / 2, guiTop + 6, 4210752);
	}

	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}

	@Override
	public void setEntryValue(int id, boolean value) {}

	@Override
	public void setEntryValue(int id, float value) {}

	@Override
	public void setEntryValue(int id, String value)
	{
		if (value.trim().isEmpty())
		{
			frequency = 0;
			doneButton.enabled = false;
		}
		else
		{
			doneButton.enabled = true;
			int freq = Integer.parseInt(value);
			if (freq <= 0xffff)
				frequency = (short) freq;
			else
				doneButton.enabled = false;
		}

	}
}
