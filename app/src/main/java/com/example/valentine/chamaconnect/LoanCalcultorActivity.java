package com.example.valentine.chamaconnect;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class LoanCalcultorActivity extends AppCompatActivity {
    private EditText mLoanAmount, mInterestRate, mLoanPeriod;
    private TextView mMontlyPaymentResult, mTotalPaymentsResult;
    private Button btncall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_calcultor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mLoanAmount = (EditText) findViewById(R.id.loan_amount);
        mInterestRate = (EditText) findViewById(R.id.interest_rate);
        mLoanPeriod = (EditText) findViewById(R.id.loan_period);
        mMontlyPaymentResult = (TextView) findViewById(R.id.monthly_payment_result);
        mTotalPaymentsResult = (TextView) findViewById(R.id.total_payments_result);
        btncall = (Button) findViewById(R.id.call);


        btncall.setOnClickListener(new View.OnClickListener() {

                                               @Override
                                               public void onClick(View arg0) {
                                                   try {
                                                       String number = "0728057123";
                                                       Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                       callIntent.setData(Uri.parse("tel:" + number));
                                                       startActivity(callIntent);
                                                   } catch(Exception e) {

                                                       Toast.makeText(getApplicationContext(),"Your call has failed...",

                                                               Toast.LENGTH_LONG).show();

                                                       e.printStackTrace();

                                                   }

                                               }

    }

    );
}

    public void showLoanPayments(View clickedButton) {

        double loanAmount = Integer.parseInt(mLoanAmount.getText().toString());
        double interestRate = (Integer.parseInt(mInterestRate.getText().toString()));
        double loanPeriod = Integer.parseInt(mLoanPeriod.getText().toString());
        double r = interestRate/1200;
        double r1 = Math.pow(r+1,loanPeriod);

        double monthlyPayment = (double) ((r+(r/(r1-1))) * loanAmount);
        double totalPayment = monthlyPayment * loanPeriod;

        mMontlyPaymentResult.setText(new DecimalFormat("##.##").format(monthlyPayment));
        mTotalPaymentsResult.setText(new DecimalFormat("##.##").format(totalPayment));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cogwheel, menu);

        return true;
    }

}
