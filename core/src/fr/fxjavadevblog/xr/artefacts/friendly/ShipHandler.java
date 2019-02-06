package fr.fxjavadevblog.xr.artefacts.friendly;

import com.badlogic.gdx.Gdx;

import fr.fxjavadevblog.xr.commons.Global;
import fr.fxjavadevblog.xr.commons.UserControls;
import fr.fxjavadevblog.xr.commons.UserControls.Control;
import fr.fxjavadevblog.xr.commons.utils.GdxCommons;

/**
 * cette classe change l'état du vaisseau en fonction des entrées du clavier.
 * cette classe génère les tirs.
 * 
 * @author robin
 *
 */
public final class ShipHandler
{
	private static ShipInput.Horizontal hControl;
	private static ShipInput.Vertical vControl;

	private ShipHandler()
	{
		// protection
	}

	public static ShipInput.Horizontal getHorizontalControl()
	{
		return hControl;
	}

	public static ShipInput.Vertical getVerticalControl()
	{
		return vControl;
	}

	public static void handle(Ship ship)
	{
		checkVerticalMove(ship);
		checkHorizontalMove(ship);
		checkShield(ship);
		checkSecondWeapon(ship);
		handleInertia(ship);
	}

	private static void checkVerticalMove(Ship ship)
	{
		vControl = ShipInput.Vertical.NONE;
		/* précondition au mouvement : que les 2 touches ne soient pas enfoncées */
		int keyUp = UserControls.get(Control.UP);
		int keyDown = UserControls.get(Control.DOWN);

		if (GdxCommons.checkConcurrentKeys(keyUp, keyDown)) return;

		float vY = ship.getVectorY();

		if (Gdx.input.isKeyPressed(keyUp))
		{
			vControl = ShipInput.Vertical.UP;
			vY += Global.SHIP_ACCELLERATION;
			ship.setVectorY(vY > Global.SHIP_SPEED ? Global.SHIP_SPEED : vY);
		}
		else if (Gdx.input.isKeyPressed(keyDown))
		{
			vControl = ShipInput.Vertical.DOWN;
			vY -= Global.SHIP_ACCELLERATION;
			ship.setVectorY(vY < -Global.SHIP_SPEED ? -Global.SHIP_SPEED : vY);
		}
	}

	private static void checkHorizontalMove(Ship ship)
	{
		hControl = ShipInput.Horizontal.NONE;

		int keyLeft = UserControls.get(Control.LEFT);
		int keyRight = UserControls.get(Control.RIGHT);

		/* précondition au mouvement : que les 2 touches ne soient pas enfoncées */
		if (GdxCommons.checkConcurrentKeys(keyLeft, keyRight)) return;

		float vX = ship.getVectorX();

		if (Gdx.input.isKeyPressed(keyLeft))
		{
			hControl = ShipInput.Horizontal.LEFT;
			vX -= Global.SHIP_ACCELLERATION;
			ship.setVectorX(vX < -Global.SHIP_SPEED ? -Global.SHIP_SPEED : vX);
		}
		else if (Gdx.input.isKeyPressed(keyRight))
		{
			hControl = ShipInput.Horizontal.RIGHT;
			vX += Global.SHIP_ACCELLERATION;
			ship.setVectorX(vX > Global.SHIP_SPEED ? Global.SHIP_SPEED : vX);
		}
	}

	private static void handleInertia(Ship ship)
	{
		float vX = ship.getVectorX();
		float vY = ship.getVectorY();

		if (hControl == ShipInput.Horizontal.NONE)
		{
			ship.setVectorX(computeInertia(vX));
		}

		if (vControl == ShipInput.Vertical.NONE)
		{
			ship.setVectorY(computeInertia(vY));
		}
	}

	private static float computeInertia(float v)
	{
		float result = v;
		if (v > 0)
		{
			result -= Global.SHIP_ACCELLERATION / 2;
		}
		else if (v < 0)
		{
			result += Global.SHIP_ACCELLERATION / 2;
		}
		return result;
	}

	private static void checkShield(Ship ship)
	{
		if (Gdx.input.isKeyJustPressed(UserControls.get(Control.SHIELD)))
		{
			ship.switchShield();
		}
	}

	private static void checkSecondWeapon(Ship ship)
	{
		if (Gdx.input.isKeyPressed(UserControls.get(Control.CHARGE_WEAPON)))
		{
			ship.weaponCharge();
		}
		else
		{
			ship.getSecondaryWeapon().fullDischarge();
		}
	}
}
