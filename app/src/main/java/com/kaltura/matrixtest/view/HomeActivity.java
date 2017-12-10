package com.kaltura.matrixtest.view;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kaltura.matrixtest.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by swapnil.d.gawande on 10/12/17.
 * This activity will display a N*N matrix based on user input a value of N, also provide functionality to transpose the matrix.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    /**
     * Matrix min and max size, user input is validated against these values
     */
    private static final int MATRIX_MIN_SIZE = 1;
    private static final int MATRIX_MAX_SIZE = 10;

    /**
     * Upper bound for random number generator
     */
    private static final int RANDOM_NUMBER_BOUND = 99;

    /**
     * UI Components
     */
    private TextInputLayout inputLayoutMatrixSize;
    private EditText etMatrixSize;
    private RecyclerView recyclerViewMatrix;
    private Button btnTranspose;

    /**
     * Adapter for RecyclerView which will show N*N matrix
     */
    private MatrixAdapter matrixAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initUI();
    }

    /**
     * Method to initialize UI componenets
     */
    private void initUI() {
        inputLayoutMatrixSize = findViewById(R.id.inputLayoutMatrixSize);
        etMatrixSize = findViewById(R.id.etMatrixSize);
        etMatrixSize.addTextChangedListener(textWatcher);
        etMatrixSize.setOnEditorActionListener(editorActionListener);

        recyclerViewMatrix = findViewById(R.id.recyclerViewMatrix);
        recyclerViewMatrix.setHasFixedSize(true);

        findViewById(R.id.btnGo).setOnClickListener(this);

        btnTranspose = findViewById(R.id.btnTranspose);
        btnTranspose.setVisibility(View.GONE);
        btnTranspose.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGo:
                hideKeyboad();
                showMatrix();
                break;
            case R.id.btnTranspose:
                transposeMatrix();
                break;
        }
    }

    /**
     * Method to generate 2D array of random integers and set it to RecyclerView adapter.
     */
    private void showMatrix() {
        if (validateMatrixSize()) {
            btnTranspose.setVisibility(View.VISIBLE);

            int size = Integer.parseInt(etMatrixSize.getText().toString());
            matrixAdapter = new MatrixAdapter(this, size, getMatrixData(size));

            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), size);
            recyclerViewMatrix.setLayoutManager(layoutManager);

            recyclerViewMatrix.setAdapter(matrixAdapter);
        }
    }

    /**
     * Method to transpose the matrix
     */
    private void transposeMatrix() {
        if (matrixAdapter != null) {
            matrixAdapter.transposeMatrix();
        } else {
            Toast.makeText(this, R.string.warn_matrix_not_available, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Method to generate matrix data
     *
     * @param size the size of matrix i.e. 'N'
     * @return the list representation of 'N*N' matrix
     */
    private ArrayList<Integer> getMatrixData(int size) {
        int matrixSize = size * size;
        Random random = new Random();
        ArrayList<Integer> matrixDataList = new ArrayList<>();
        for (int i = 0; i < matrixSize; i++) {
            matrixDataList.add(random.nextInt(RANDOM_NUMBER_BOUND));
        }
        return matrixDataList;
    }

    /**
     * Method to validate matrix size
     *
     * @return true, if matrix size is valid; false otherwise
     */
    private boolean validateMatrixSize() {
        String matrixSize = etMatrixSize.getText().toString();

        if (TextUtils.isEmpty(matrixSize)) {
            inputLayoutMatrixSize.setError(getString(R.string.err_blank_matrix_size));
            requestFocus(etMatrixSize);
            return false;
        } else if (!isValidMatrixSize(matrixSize)) {
            inputLayoutMatrixSize.setError(getString(R.string.err_invalid_matrix_size));
            requestFocus(etMatrixSize);
            return false;
        } else {
            inputLayoutMatrixSize.setErrorEnabled(false);
        }

        return true;
    }

    /**
     * Method to check if matrix size is in given range
     *
     * @param matrixSize the size of matrix i.e. 'N'
     * @return
     */
    private boolean isValidMatrixSize(String matrixSize) {
        try {
            int size = Integer.parseInt(matrixSize);
            return (MATRIX_MIN_SIZE < size && size <= MATRIX_MAX_SIZE);
        } catch (NumberFormatException nfe) {
            //ignore
        }
        return false;
    }

    /**
     * Method to request focus for given view
     *
     * @param view the view to be focused
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Textwatcher for matrix size edittext, it validates the user input
     */
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            validateMatrixSize();
        }
    };

    /**
     * OnEditorActionListener to perform required operation on click of keyboard 'Done' button
     */
    private final TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboad();
                showMatrix();
                return true;
            }
            return false;
        }
    };

    /**
     * Method to hide soft-keyboard
     */
    private void hideKeyboad() {
        InputMethodManager in = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(etMatrixSize.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
