package rzk.wirelessredstone.tile;

import rzk.wirelessredstone.registry.ModTiles;

public class TileTransmitter extends TileFrequency
{
	public TileTransmitter()
	{
		super(ModTiles.transmitter);
	}

	@Override
	public Type getDeviceType()
	{
		return Type.TRANSMITTER;
	}
}
