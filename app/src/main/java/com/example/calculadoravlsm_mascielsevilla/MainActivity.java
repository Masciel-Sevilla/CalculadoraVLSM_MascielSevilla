package com.example.calculadoravlsm_mascielsevilla;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.drawable.GradientDrawable;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    private EditText ipInput;
    private EditText subnetMaskInput;
    private EditText numSubnetsInput;
    private Button addButton;
    private Button calculateButton;
    private Button clearButton;
    private TableLayout tableLayout;
    private TableLayout resultsTableLayout;
    private ArrayList<Subred> redesdisponibles;

    // Updated color scheme for better visual appeal
    private final int COLOR_PRIMARY = Color.parseColor("#4A5568");
    private final int COLOR_PRIMARY_DARK = Color.parseColor("#2D3748");
    private final int COLOR_ACCENT = Color.parseColor("#6B46C1");
    private final int COLOR_HEADER_BG = Color.parseColor("#6B46C1");  // Purple header
    private final int COLOR_HEADER_TEXT = Color.parseColor("#FFFFFF");
    private final int COLOR_ROW_ODD = Color.parseColor("#EDF2F7");
    private final int COLOR_ROW_EVEN = Color.parseColor("#FFFFFF");
    private final int COLOR_BORDER = Color.parseColor("#CBD5E0");
    private final int COLOR_TEXT = Color.parseColor("#2D3748");
    private final int COLOR_TITLE_BG = Color.parseColor("#805AD5");  // Light purple for title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        ipInput = findViewById(R.id.ipInput);
        subnetMaskInput = findViewById(R.id.subnetMaskInput);
        numSubnetsInput = findViewById(R.id.numSubnetsInput);
        addButton = findViewById(R.id.addButton);
        calculateButton = findViewById(R.id.calculateButton);
        clearButton = findViewById(R.id.clearButton);
        tableLayout = findViewById(R.id.tableLayout);
        resultsTableLayout = findViewById(R.id.resultsTableLayout);


        // Style the buttons
        styleButtons();

        // Disable calculate button initially
        calculateButton.setEnabled(false);
        clearButton.setEnabled(false);

        // Set hints and input type for IP and mask
        ipInput.setHint("Dirección IP (192.168.1.0)");
        subnetMaskInput.setHint("Máscara de red (0-32)");
    }

    // Create a title section with app name and author credits
    private void createTitleSection() {
        // Find the root layout - assuming it's a LinearLayout
        LinearLayout rootLayout = findViewById(R.id.rootLayout);

        // Create title container
        LinearLayout titleContainer = new LinearLayout(this);
        titleContainer.setOrientation(LinearLayout.VERTICAL);
        titleContainer.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        titleContainer.setBackgroundColor(COLOR_TITLE_BG);
        titleContainer.setPadding(0, 32, 0, 32);
        titleContainer.setGravity(Gravity.CENTER);

        // Create and style app title
        TextView titleView = new TextView(this);
        titleView.setText("CALCULADORA VLSM");
        titleView.setTextSize(24);
        titleView.setTypeface(null, Typeface.BOLD);
        titleView.setTextColor(Color.WHITE);
        titleView.setGravity(Gravity.CENTER);

        // Create and style author credit
        TextView authorView = new TextView(this);
        authorView.setText("Realizado por: Masciel Sevilla");
        authorView.setTextSize(16);
        authorView.setTextColor(Color.WHITE);
        authorView.setGravity(Gravity.CENTER);
        authorView.setPadding(0, 8, 0, 0);

        // Add views to container
        titleContainer.addView(titleView);
        titleContainer.addView(authorView);

        // Add title container as the first element in the root layout
        rootLayout.addView(titleContainer, 0);
    }

    // Style the buttons for a more modern look
    private void styleButtons() {
        // Create a reusable button style
        GradientDrawable buttonBackground = new GradientDrawable();
        buttonBackground.setColor(COLOR_ACCENT);
        buttonBackground.setCornerRadius(8);

        // Apply to Add button
        addButton.setBackground(buttonBackground);
        addButton.setTextColor(Color.WHITE);
        addButton.setPadding(16, 12, 16, 12);

        // Create background for Calculate button
        GradientDrawable calculateBackground = new GradientDrawable();
        calculateBackground.setColor(COLOR_PRIMARY);
        calculateBackground.setCornerRadius(8);
        calculateButton.setBackground(calculateBackground);
        calculateButton.setTextColor(Color.WHITE);
        calculateButton.setPadding(16, 12, 16, 12);

        // Create background for Clear button
        GradientDrawable clearBackground = new GradientDrawable();
        clearBackground.setColor(Color.parseColor("#E53E3E"));  // Red color for clear
        clearBackground.setCornerRadius(8);
        clearButton.setBackground(clearBackground);
        clearButton.setTextColor(Color.WHITE);
        clearButton.setPadding(16, 12, 16, 12);
    }

    // Create a styled TextView for tables
    private TextView createStyledTextView(String text, int backgroundColor, int textColor, boolean isHeader) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(textColor);
        textView.setPadding(16, 12, 16, 12);
        textView.setGravity(Gravity.CENTER);

        if (isHeader) {
            textView.setTypeface(null, Typeface.BOLD);
            textView.setTextSize(16);
        } else {
            textView.setTextSize(14);
        }

        // Create border
        GradientDrawable border = new GradientDrawable();
        border.setColor(backgroundColor);
        border.setStroke(1, COLOR_BORDER);
        textView.setBackground(border);

        return textView;
    }

    // Create a styled EditText for host inputs
    private EditText createStyledEditText() {
        EditText editText = new EditText(this);
        editText.setTag("inputhost");
        editText.setHint("Hosts");
        editText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        editText.setGravity(Gravity.CENTER);
        editText.setPadding(16, 8, 16, 8);

        // Create border
        GradientDrawable border = new GradientDrawable();
        border.setColor(Color.WHITE);
        border.setStroke(1, COLOR_BORDER);
        border.setCornerRadius(4);
        editText.setBackground(border);

        return editText;
    }

    // Called when 'Agregar' button is clicked
    public void agregar(View view) {
        try {
            int numSubnets = Integer.parseInt(numSubnetsInput.getText().toString());
            if (numSubnets <= 0) {
                Toast.makeText(this, "El número de subredes debe ser mayor que cero", Toast.LENGTH_SHORT).show();
                return;
            }

            tableLayout.removeAllViews();

            // Create table header with styling
            TableRow headerRow = new TableRow(this);
            headerRow.setLayoutParams(new TableLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));

            TextView headerNumSubnet = createStyledTextView("Número", COLOR_HEADER_BG, COLOR_HEADER_TEXT, true);
            TextView headerNumHosts = createStyledTextView("Cantidad de Hosts", COLOR_HEADER_BG, COLOR_HEADER_TEXT, true);

            headerRow.addView(headerNumSubnet);
            headerRow.addView(headerNumHosts);
            tableLayout.addView(headerRow);

            // Create rows with input fields for each subnet
            for (int i = 0; i < numSubnets; i++) {
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableLayout.LayoutParams(
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));

                int rowColor = (i % 2 == 0) ? COLOR_ROW_EVEN : COLOR_ROW_ODD;

                TextView subnetNumber = createStyledTextView(String.valueOf(i + 1), rowColor, COLOR_TEXT, false);
                EditText hostInput = createStyledEditText();

                row.addView(subnetNumber);
                row.addView(hostInput);
                tableLayout.addView(row);
            }

            // Update button states
            calculateButton.setEnabled(true);
            addButton.setEnabled(false);
            clearButton.setEnabled(true);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor, ingrese un número válido de subredes", Toast.LENGTH_SHORT).show();
        }
    }

    // Called when 'Calcular' button is clicked
    public void calcular(View view) {
        calculateButton.setEnabled(false);

        try {
            // Validate inputs first
            String ipAddress = ipInput.getText().toString();
            String subnetMaskStr = subnetMaskInput.getText().toString();

            if (ipAddress.isEmpty() || subnetMaskStr.isEmpty()) {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                calculateButton.setEnabled(true);
                return;
            }

            // Validate IP address format (simple check)
            if (!ipAddress.matches("^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$")) {
                Toast.makeText(this, "Formato de IP inválido. Use formato: xxx.xxx.xxx.xxx", Toast.LENGTH_SHORT).show();
                calculateButton.setEnabled(true);
                return;
            }

            // Validate each octet of IP address
            String[] octets = ipAddress.split("\\.");
            for (String octet : octets) {
                int value = Integer.parseInt(octet);
                if (value < 0 || value > 255) {
                    Toast.makeText(this, "Cada octeto de la IP debe estar entre 0 y 255", Toast.LENGTH_SHORT).show();
                    calculateButton.setEnabled(true);
                    return;
                }
            }

            // Validate subnet mask (ensure it's between 0 and 32)
            int maskValue;
            try {
                maskValue = Integer.parseInt(subnetMaskStr);
                if (maskValue < 0 || maskValue > 32) {
                    Toast.makeText(this, "Máscara de red debe estar entre 0 y 32", Toast.LENGTH_SHORT).show();
                    calculateButton.setEnabled(true);
                    return;
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La máscara debe ser un número entre 0 y 32", Toast.LENGTH_SHORT).show();
                calculateButton.setEnabled(true);
                return;
            }

            ArrayList<Integer> hostData = obtenerDatos();

            if (hostData.isEmpty()) {
                Toast.makeText(this, "Por favor, ingrese al menos una cantidad de hosts", Toast.LENGTH_SHORT).show();
                calculateButton.setEnabled(true);
                return;
            }

            // Create parent network
            DireccionIp direccionPadre = new DireccionIp(ipAddress, maskValue, 'd');
            direccionPadre.aDireccionDecimal();
            Subred subRedPadre = new Subred(direccionPadre);

            // Initialize available networks list
            redesdisponibles = new ArrayList<>();
            redesdisponibles.add(subRedPadre);

            crearTablaResultado();

            for (int i = 0; i < hostData.size(); i++) {
                subneteo(hostData.get(i), i);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Error de formato: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            calculateButton.setEnabled(true);
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            calculateButton.setEnabled(true);
            e.printStackTrace();
        }
    }

    // Called when 'Limpiar' button is clicked
    public void limpiar(View view) {
        // Reset all inputs and tables
        ipInput.setText("");
        subnetMaskInput.setText("");
        numSubnetsInput.setText("");
        tableLayout.removeAllViews();
        resultsTableLayout.removeAllViews();

        // Reset button states
        addButton.setEnabled(true);
        calculateButton.setEnabled(false);
        clearButton.setEnabled(false);
    }

    // Collects host data from input fields and sorts them in descending order
    private ArrayList<Integer> obtenerDatos() {
        ArrayList<Integer> hostData = new ArrayList<>();

        for (int i = 1; i < tableLayout.getChildCount(); i++) { // Start at 1 to skip header row
            View child = tableLayout.getChildAt(i);
            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;
                for (int j = 0; j < row.getChildCount(); j++) {
                    View cell = row.getChildAt(j);
                    if (cell instanceof EditText && "inputhost".equals(cell.getTag())) {
                        String value = ((EditText) cell).getText().toString();
                        if (!value.isEmpty()) {
                            hostData.add(Integer.parseInt(value));
                        }
                    }
                }
            }
        }

        // Sort in descending order
        Collections.sort(hostData, new Comparator<Integer>() {
            @Override
            public int compare(Integer a, Integer b) {
                return b.compareTo(a);
            }
        });

        return hostData;
    }

    // Creates result table with headers
    private void crearTablaResultado() {
        resultsTableLayout.removeAllViews();

        // Create a styled header for results
        TextView resultadoHeader = new TextView(this);
        resultadoHeader.setText("Resultados del Cálculo VLSM");
        resultadoHeader.setTextSize(18);
        resultadoHeader.setTypeface(null, Typeface.BOLD);
        resultadoHeader.setPadding(16, 24, 16, 24);
        resultadoHeader.setGravity(Gravity.CENTER);
        resultadoHeader.setTextColor(Color.WHITE);

        // Create a nice background for the header
        GradientDrawable headerBackground = new GradientDrawable();
        headerBackground.setColor(COLOR_ACCENT);
        headerBackground.setCornerRadii(new float[]{8, 8, 8, 8, 0, 0, 0, 0});  // Round top corners
        resultadoHeader.setBackground(headerBackground);

        // Set layout parameters
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 24, 0, 0);  // Add some top margin
        resultadoHeader.setLayoutParams(params);

        // Add to layout
        resultsTableLayout.addView(resultadoHeader);

        // Create table header
        TableRow headerRow = new TableRow(this);
        headerRow.setLayoutParams(new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        String[] headers = {"Número de Host", "IP de Red", "Máscara", "Primer Host", "Último Host", "Broadcast"};
        for (String header : headers) {
            TextView headerText = createStyledTextView(header, COLOR_HEADER_BG, COLOR_HEADER_TEXT, true);
            headerRow.addView(headerText);
        }

        resultsTableLayout.addView(headerRow);
    }

    // Adds a row to the results table
    private void agregarFilaATabla(int numHost, String dirRed, int bitMascara, String primeraDirHo,
                                   String ultimaDirHo, String dirBroadcast) {
        int rowIndex = resultsTableLayout.getChildCount();
        int rowColor = (rowIndex % 2 == 0) ? COLOR_ROW_ODD : COLOR_ROW_EVEN;

        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));

        // Create styled text views for each cell
        TextView hostText = createStyledTextView(String.valueOf(numHost), rowColor, COLOR_TEXT, false);
        TextView dirRedText = createStyledTextView(dirRed, rowColor, COLOR_TEXT, false);
        TextView mascaraText = createStyledTextView(String.valueOf(bitMascara), rowColor, COLOR_TEXT, false);
        TextView primerHostText = createStyledTextView(primeraDirHo, rowColor, COLOR_TEXT, false);
        TextView ultimoHostText = createStyledTextView(ultimaDirHo, rowColor, COLOR_TEXT, false);
        TextView broadcastText = createStyledTextView(dirBroadcast, rowColor, COLOR_TEXT, false);

        row.addView(hostText);
        row.addView(dirRedText);
        row.addView(mascaraText);
        row.addView(primerHostText);
        row.addView(ultimoHostText);
        row.addView(broadcastText);

        resultsTableLayout.addView(row);
    }

    // Subnetting algorithm
    private void subneteo(int numHost, int indiceHost) {
        int numbitHostNece = 1;

        while (Math.pow(2, numbitHostNece) - 2 < numHost) {
            numbitHostNece++;
        }

        Subred dtrabjando = redesdisponibles.get(0);

        if (numbitHostNece == dtrabjando.getDireccionRed().getBithost()) {
            // Add to table
            agregarFilaATabla(
                    numHost,
                    dtrabjando.getDireccionRed().getDireccionDecimal(),
                    dtrabjando.getDireccionRed().getBitmascara(),
                    dtrabjando.getPrimeraDireccionHo().getDireccionDecimal(),
                    dtrabjando.getUltimaDireccionHo().getDireccionDecimal(),
                    dtrabjando.getDireccionBroadcast().getDireccionDecimal()
            );
            // Remove from array
            redesdisponibles.remove(0);
        } else {
            redesdisponibles.remove(0);

            int numSubredesTotal = (int) Math.pow(2, 32 - dtrabjando.getDireccionRed().getBitmascara() - numbitHostNece);
            String parteRed = dtrabjando.getDireccionRed().getDireccionBinario().substring(0, dtrabjando.getDireccionRed().getBitmascara());
            String parteDerecha = dtrabjando.getDireccionRed().getDireccionBinario().substring(32 - numbitHostNece);

            int numerobitsCambiar = 32 - dtrabjando.getDireccionRed().getBitmascara() - numbitHostNece;

            for (int i = 0; i < numSubredesTotal; i++) {
                String numaux = padLeftZeros(Integer.toBinaryString(i), numerobitsCambiar);
                Subred subaux = new Subred(new DireccionIp(parteRed + numaux + parteDerecha, 32 - numbitHostNece, 'b'));

                if (indiceHost <= 0) {
                    redesdisponibles.add(subaux);
                } else {
                    // Insert in order
                    if (i <= redesdisponibles.size() && i == 0) {
                        redesdisponibles.add(i, subaux);
                    } else {
                        // Subtract one because the first was removed
                        redesdisponibles.add(i - 1, subaux);
                    }
                }

                // Print the first
                if (i == 0) {
                    // Add to table
                    agregarFilaATabla(
                            numHost,
                            subaux.getDireccionRed().getDireccionDecimal(),
                            subaux.getDireccionRed().getBitmascara(),
                            subaux.getPrimeraDireccionHo().getDireccionDecimal(),
                            subaux.getUltimaDireccionHo().getDireccionDecimal(),
                            subaux.getDireccionBroadcast().getDireccionDecimal()
                    );
                    // Remove from array
                    redesdisponibles.remove(0);
                }
            }
        }
    }

    // Helper method to pad binary strings with leading zeros
    private String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);
        return sb.toString();
    }

    // IP Address class
    private class DireccionIp {
        private int bitmascara;
        private int bithost;
        private String direccionDecimal;
        private String direccionBinario;

        public DireccionIp(String direccion, int bitmascara, char tipo) {
            this.bitmascara = bitmascara;
            this.bithost = 32 - bitmascara;

            if (tipo == 'd') {
                this.direccionDecimal = direccion;
                this.direccionBinario = aDireccionBinario();
            } else {
                this.direccionBinario = direccion;
                this.direccionDecimal = aDireccionDecimal();
            }
        }

        public int getBithost() {
            return bithost;
        }

        public void setBithost(int value) {
            this.bithost = value;
        }

        public int getBitmascara() {
            return bitmascara;
        }

        public void setBitmascara(int value) {
            this.bitmascara = value;
        }

        public String getDireccionDecimal() {
            return direccionDecimal;
        }

        public void setDireccionDecimal(String value) {
            this.direccionDecimal = value;
        }

        public String getDireccionBinario() {
            return direccionBinario;
        }

        public void setDireccionBinario(String value) {
            this.direccionBinario = value;
        }

        public String aDireccionBinario() {
            String[] octetos = direccionDecimal.split("\\.");
            StringBuilder direccionBi = new StringBuilder();

            for (int i = 0; i < octetos.length; i++) {
                int octetoDecimal = Integer.parseInt(octetos[i]);

                if (octetoDecimal >= 0 && octetoDecimal <= 255) {
                    direccionBi.append(padLeftZeros(Integer.toBinaryString(octetoDecimal), 8));
                } else {
                    throw new IllegalArgumentException("La dirección decimal no es válida.");
                }
            }

            return direccionBi.toString();
        }

        public String aDireccionDecimal() {
            StringBuilder respTotal = new StringBuilder();

            for (int i = 0; i < 4; i++) {
                String respuestaOcteto = direccionBinario.substring(i * 8, (i * 8) + 8);
                int octetoDecimal = Integer.parseInt(respuestaOcteto, 2);
                respTotal.append(octetoDecimal).append(".");
            }

            return respTotal.substring(0, respTotal.length() - 1); // Eliminar el último punto
        }
    }

    // Subnet class
    private class Subred {
        private DireccionIp direccionRed;
        private DireccionIp direccionBroadcast;
        private DireccionIp primeraDireccionHo;
        private DireccionIp ultimaDireccionHo;

        public Subred(DireccionIp direccionRed) {
            this.direccionRed = direccionRed;

            String porcionRed = direccionRed.getDireccionBinario().substring(0, direccionRed.getBitmascara());

            // Create broadcast address (all 1s in host portion)
            StringBuilder broadcastBinary = new StringBuilder(porcionRed);
            for (int i = 0; i < (32 - porcionRed.length()); i++) {
                broadcastBinary.append("1");
            }
            this.direccionBroadcast = new DireccionIp(broadcastBinary.toString(), direccionRed.getBitmascara(), 'b');

            // Create first host address (network address + 1)
            StringBuilder firstHostBinary = new StringBuilder(porcionRed);
            for (int i = 0; i < (31 - porcionRed.length()); i++) {
                firstHostBinary.append("0");
            }
            firstHostBinary.append("1");
            this.primeraDireccionHo = new DireccionIp(firstHostBinary.toString(), direccionRed.getBitmascara(), 'b');

            // Create last host address (broadcast - 1)
            StringBuilder lastHostBinary = new StringBuilder(porcionRed);
            for (int i = 0; i < (31 - porcionRed.length()); i++) {
                lastHostBinary.append("1");
            }
            lastHostBinary.append("0");
            this.ultimaDireccionHo = new DireccionIp(lastHostBinary.toString(), direccionRed.getBitmascara(), 'b');
        }

        public DireccionIp getDireccionRed() {
            return direccionRed;
        }

        public DireccionIp getDireccionBroadcast() {
            return direccionBroadcast;
        }

        public DireccionIp getPrimeraDireccionHo() {
            return primeraDireccionHo;
        }

        public DireccionIp getUltimaDireccionHo() {
            return ultimaDireccionHo;
        }
    }
}