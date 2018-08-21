package wallet.bitcoin.bitcoinwallet.helper;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

import java.util.HashMap;
import java.util.Map;

public class MagicDrawable extends StateListDrawable {

    private Map<Integer, Integer> stateColorsMap;
    private int defaultColor;

    public static MagicDrawable create(Drawable drawable, int defaultColor, int pressedColor, int selectedColor) {
        Map<Integer, Integer> map = new HashMap();
        map.put(android.R.attr.state_selected, selectedColor);
        map.put(android.R.attr.state_pressed, pressedColor);

        MagicDrawable stateDrawable = new MagicDrawable(drawable, defaultColor, map);
        return stateDrawable;
    }

    public static MagicDrawable createSelected(Drawable drawable, int defaultColor, int selectedColor) {
        Map<Integer, Integer> map = new HashMap();
        map.put(android.R.attr.state_selected, selectedColor);

        MagicDrawable stateDrawable = new MagicDrawable(drawable, defaultColor, map);
        return stateDrawable;
    }

    public static MagicDrawable createEnabled(Drawable drawable, int disabledColor, int enabledColor) {
        Map<Integer, Integer> map = new HashMap();
        map.put(android.R.attr.state_enabled, enabledColor);

        MagicDrawable stateDrawable = new MagicDrawable(drawable, disabledColor, map);
        return stateDrawable;
    }

    public static MagicDrawable createPressed(Drawable drawable, int defaultColor, int pressedColor) {
        Map<Integer, Integer> map = new HashMap();
        map.put(android.R.attr.state_pressed, pressedColor);

        MagicDrawable stateDrawable = new MagicDrawable(drawable, defaultColor, map);
        return stateDrawable;
    }

    /**
     * @param drawableNorm     DEF
     * @param defaultColor     DEF
     * @param drawableSelected Selected
     * @param selectedColor
     * @param drawablePressed  Pressed
     * @param pressedColor
     * @return
     */
    public static MagicDrawable createtabletTab(Drawable drawableNorm, int defaultColor, Drawable drawableSelected, int selectedColor, Drawable drawablePressed, int pressedColor) {
        Map<Integer, Integer> map = new HashMap();
        map.put(android.R.attr.state_selected, selectedColor);
        map.put(android.R.attr.state_pressed, pressedColor);

        MagicDrawable stateDrawable = new MagicDrawable();

        stateDrawable.stateColorsMap = map;
        stateDrawable.defaultColor = defaultColor;

        drawableNorm.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);

        stateDrawable.addState(new int[]{android.R.attr.state_selected}, drawableSelected);
        stateDrawable.addState(new int[]{android.R.attr.state_pressed}, drawablePressed);

        stateDrawable.addState(new int[]{}, drawableNorm);

        return stateDrawable;
    }

    public static MagicDrawable createCheckBox(Drawable drawableNorm, int defaultColor, Drawable drawableChecked, int checkedColor) {
        Map<Integer, Integer> map = new HashMap();
        map.put(android.R.attr.state_checked, checkedColor);

        MagicDrawable stateDrawable = new MagicDrawable();

        stateDrawable.stateColorsMap = map;
        stateDrawable.defaultColor = defaultColor;

        drawableNorm.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);

        stateDrawable.addState(new int[]{android.R.attr.state_checked}, drawableChecked);
        stateDrawable.addState(new int[]{}, drawableNorm);

        return stateDrawable;
    }

    /**
     * Create state list drawable programmatically - just pass drawable, default color of drawable, and Map of (state , color)
     *
     * @param drawable       resourse for icons
     * @param defaultColor   color for normal state
     * @param stateColorsMap map of for ex. android.R.attr.state_pressed and ColorManager.someColor
     *                       <p>
     *                       Map<Integer, Integer> map = new HashMap<>();
     *                       map.put(android.R.attr.state_pressed, ColorManager.rose);
     */
    public MagicDrawable(Drawable drawable, int defaultColor, Map<Integer, Integer> stateColorsMap) {
        super();
        this.stateColorsMap = stateColorsMap;
        this.defaultColor = defaultColor;

        drawable.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);

        if (stateColorsMap != null) {
            for (int state : stateColorsMap.keySet()) {
                addState(new int[]{state}, drawable);
            }
        }

        addState(new int[]{}, drawable);
    }

    public MagicDrawable() {

    }

    @Override
    protected boolean onStateChange(int[] states) {

        if (stateColorsMap == null) {
            super.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);
            return super.onStateChange(states);
        }

        boolean colorSet = false;

        if (states != null) {
            for (int state : states) {

                for (int st : stateColorsMap.keySet()) {

                    if (state == st) {
                        super.setColorFilter(stateColorsMap.get(st), PorterDuff.Mode.SRC_IN);
                        colorSet = true;
                        break;
                    }
                }
            }
        }

        if (!colorSet) {
            super.setColorFilter(defaultColor, PorterDuff.Mode.SRC_IN);
        }

        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

}

