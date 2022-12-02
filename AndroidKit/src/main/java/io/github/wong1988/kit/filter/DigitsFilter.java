package io.github.wong1988.kit.filter;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

public class DigitsFilter implements InputFilter {

    private final String mAllowedDigits;

    public DigitsFilter(String digits) {
        if (digits == null)
            digits = "";
        mAllowedDigits = digits;
    }

    /**
     * This method is called when the buffer is going to replace the
     * range <code>dstart &hellip; dend</code> of <code>dest</code>
     * with the new text from the range <code>start &hellip; end</code>
     * of <code>source</code>.  Returns the CharSequence that we want
     * placed there instead, including an empty string
     * if appropriate, or <code>null</code> to accept the original
     * replacement.  Be careful to not to reject 0-length replacements,
     * as this is what happens when you delete text.
     */
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        onStart();

        // Scan through beginning characters in dest, calling onInvalidCharacter()
        // for each invalid character.
        for (int i = 0; i < dstart; i++) {
            char c = dest.charAt(i);
            if (!isAllowed(c)) onInvalidCharacter(c);
        }

        // Scan through changed characters rejecting disallowed chars
        SpannableStringBuilder modification = null;
        int modoff = 0;

        for (int i = start; i < end; i++) {
            char c = source.charAt(i);
            if (isAllowed(c)) {
                // Character allowed.
                modoff++;
            } else {

                if (modification == null) {
                    modification = new SpannableStringBuilder(source, start, end);
                    modoff = i - start;
                }

                modification.delete(modoff, modoff + 1);

                onInvalidCharacter(c);
            }
        }

        // Scan through remaining characters in dest, calling onInvalidCharacter()
        // for each invalid character.
        for (int i = dend; i < dest.length(); i++) {
            char c = dest.charAt(i);
            if (!isAllowed(c)) onInvalidCharacter(c);
        }

        onStop();

        // Either returns null if we made no changes,
        // or what we wanted to change it to if there were changes.
        return modification;
    }

    /**
     * Called when we start processing filter.
     */
    public void onStart() {

    }

    /**
     * Called whenever we encounter an invalid character.
     *
     * @param c the invalid character
     */
    public void onInvalidCharacter(char c) {

    }

    /**
     * Called when we're done processing filter
     */
    public void onStop() {

    }

    /**
     * Returns whether or not we allow character c.
     * Subclasses must override this method.
     */
    public boolean isAllowed(char c) {
        return mAllowedDigits.indexOf(c) != -1;
    }

}
