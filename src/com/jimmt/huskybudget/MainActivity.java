package com.jimmt.huskybudget;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
    EditText moneyTextField;
    TextView daysLeftText, moneyPerDayText;
    Button lookupButton;
    int daysLeft = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        findUIElements();
        setupDaysLeftText();
        setupLookupButton();
        setupMoneyTextField();
        //        moneyTextField.getBackground().setColorFilter(Color.parseColor(getString(R.color.gold)), PorterDuff.Mode.SRC_IN);
    }

    private void findUIElements() {
        daysLeftText = (TextView) findViewById(R.id.days_left_text);
        lookupButton = (Button) findViewById(R.id.lookup_button);
        moneyTextField = (EditText) findViewById(R.id.money_textfield);
        moneyPerDayText = (TextView) findViewById(R.id.daily_money_text);
    }

    private void setupDaysLeftText() {
        try {
            JSONObject termData = new GetTermDataTask().execute("").get();
            String str = termData.get("LastDayOfClasses").toString();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            DateTime time = new DateTime(df.parse(str));
            DateTime currentDate = new DateTime();
            daysLeft = Days.daysBetween(currentDate, time).getDays();
        } catch (Exception ex) {

        }
        daysLeftText.setText("");
        SpannableString spannableString = new SpannableString(daysLeft + " Days Left In Quarter");
        spannableString.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.gold)), 0,
        String.valueOf(daysLeft).length(), 0);
        daysLeftText.append(spannableString);
    }

    private void setupLookupButton() {
        lookupButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.hfs.washington.edu/olco/Secure/AccountSummary.aspx"));
                startActivity(browserIntent);
            }

        });
    }

    private void updateMoneyPerDayText(double moneyPerDay) {
        String formattedMoneyPerDay = NumberFormat.getCurrencyInstance().format(moneyPerDay);
        moneyPerDayText.setText("$ / Day: " + formattedMoneyPerDay);
    }

    private void setupMoneyTextField() {

        //        Selection.setSelection(moneyTextField.getText(), 1);
        moneyTextField.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().startsWith("$")) {
                    moneyTextField.setText("$" + moneyTextField.getText());
                    Selection.setSelection(moneyTextField.getText(),
                    moneyTextField.getText().length());

                }
                if (s.toString().contains(".")) {
                    int decimalPlaces = s.toString().length() - 1 - s.toString().indexOf('.');
                    if (decimalPlaces > 2) {
                        s.delete(s.length() - decimalPlaces + 2, s.length());
                    }
                }
                if (moneyTextField.getText().length() > 1) {
                    double money = Double
                    .parseDouble(moneyTextField.getText().toString().replace("$", ""));
                    updateMoneyPerDayText(money / daysLeft);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
