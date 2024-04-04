package me.bruce.calc;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EcmaError;
import org.mozilla.javascript.Scriptable;

import com.google.android.material.button.MaterialButton;

// MainActivity class which extends AppCompatActivity and implements OnClickListener
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    // Declare TextView and MaterialButton variables
    TextView resultText, solutionText;
    MaterialButton buttonC, bracketOpen, bracketClose;
    MaterialButton buttonDivide, buttonMultiply, buttonPlus, buttonMinus, buttonEquals;
    MaterialButton button0,button1,button2,button3,button4,button5,button6,button7,button8,button9;
    MaterialButton buttonAC, buttonPoint;

    @Override
    // onCreate method which is called when the activity is starting
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge display
        EdgeToEdge.enable(this);
        // Set the activity content from a layout resource
        setContentView(R.layout.activity_main);
        // Initialize TextView and MaterialButton variables
        resultText = findViewById(R.id.result_text);
        solutionText = findViewById(R.id.solution_text);
        buttonC = findViewById(R.id.button_c);
        bracketOpen = findViewById(R.id.button_open_bracket);
        bracketClose = findViewById(R.id.button_close_bracket);
        buttonDivide = findViewById(R.id.button_divide);
        buttonMultiply = findViewById(R.id.button_multiply);
        buttonPlus = findViewById(R.id.button_plus);
        buttonMinus = findViewById(R.id.button_minus);
        buttonEquals = findViewById(R.id.button_equals);
        button0 = findViewById(R.id.button_0);
        button1 = findViewById(R.id.button_1);
        button2 = findViewById(R.id.button_2);
        button3 = findViewById(R.id.button_3);
        button4 = findViewById(R.id.button_4);
        button5 = findViewById(R.id.button_5);
        button6 = findViewById(R.id.button_6);
        button7 = findViewById(R.id.button_7);
        button8 = findViewById(R.id.button_8);
        button9 = findViewById(R.id.button_9);
        buttonAC = findViewById(R.id.button_AC);
        buttonPoint = findViewById(R.id.button_point);

        // Set onClickListener for each button
        buttonC.setOnClickListener(this);
        bracketOpen.setOnClickListener(this);
        bracketClose.setOnClickListener(this);
        buttonDivide.setOnClickListener(this);
        buttonMultiply.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonEquals.setOnClickListener(this);
        button0.setOnClickListener(this);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        buttonAC.setOnClickListener(this);
        buttonPoint.setOnClickListener(this);

        // Set onApplyWindowInsetsListener for the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    // onClick method which is called when a view has been clicked
    public void onClick(View view){
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate =  solutionText.getText().toString();

        // If the button text is "AC", clear the solutionText and resultText
        if (buttonText.equals("AC")){
            solutionText.setText("");
            resultText.setText("0");
            return;
        }

        // If the button text is "=", set the solutionText to the resultText
        if(buttonText.equals("=")){
            solutionText.setText(resultText.getText());
            return;
        }

        // If the button text is "C", remove the last character from dataToCalculate
        if (buttonText.equals("C")){
            if(!dataToCalculate.isEmpty())
             dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length()-1);
        }else{
            // Otherwise, append the button text to dataToCalculate
            dataToCalculate = dataToCalculate + buttonText;
        }
        solutionText.setText(dataToCalculate);
        // If dataToCalculate contains an open bracket without a closing bracket, skip the evaluation step
        if (dataToCalculate.isEmpty() || dataToCalculate.contains("(") && !dataToCalculate.contains(")")) {
            return;
        }
        // Get the result of the calculation
        String result = getResult(dataToCalculate);

        // If the result is not "Error", set the resultText to the result
        if (!result.equals("Error")){
            resultText.setText(result);
        }
    }

    // getResult method which calculates the result of the expression in data
    String getResult(String data) {
        try {
            String result;
            // Enter a Context
            Context rhino = Context.enter();
            // Disable optimization
            rhino.setOptimizationLevel(-1);
            // If data ends with an operator, remove it
            if (data.endsWith("+") || data.endsWith("-") || data.endsWith("*") || data.endsWith("/")) {
                data = data.substring(0, data.length() - 1);
            }
            // Replace any closing bracket followed by a digit or an opening bracket with a closing bracket, a "*", and the same digit or opening bracket
            data = data.replaceAll("\\)([\\d(])", ")*$1");
            // Initialize a standard JavaScript object
            Scriptable scope = rhino.initStandardObjects();
            // Evaluate the string
            result = rhino.evaluateString(scope, data, "JavaScript", 1, null).toString();
            // If the result ends with ".0", remove it
            if (result.endsWith(".0")) {
                result = result.replace(".0", "");
            }
            return result;
        } catch (EcmaError e) {
            // Handle error: return "Error"
            return "Error";
        } finally {
            // Exit the Context
            Context.exit();
        }
    }
}