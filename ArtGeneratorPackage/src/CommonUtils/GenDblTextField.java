/* A
 * Class GenDblTextField
 * =================
 * The SGTextField class extends the TextField control to make sure that only
 * valid numbers and a dot can be entered into the TextField. Empty TextField is allowed.
 * Other symbols are ignored (replaced with empty string).
 *
 */
package CommonUtils;

import javafx.scene.control.TextField;
 
public class GenDblTextField extends TextField {
    // Method to handle direct input of chars
    @Override public void replaceText(int start, int end, String text) {
        if (("".equals(text)) || (text.matches("[0-9.]+"))) {

        } else {
            text = "";
        }
        super.replaceText(start, end, text);
    }
    // Method to deal with replacement of a selection of chars in the text
    // input field.
    @Override public void replaceSelection(String text) {
        if (("".equals(text)) || (text.matches("[0-9.]+"))) {
            super.replaceSelection(text);
        }
    }
    
    public double getValue() {
        if(getText().isEmpty()) {
            return 0;
        } else {
            return Double.parseDouble(getText());
        }

    }
    
}
