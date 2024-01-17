package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.text.pdf.PdfWriter;

import org.w3c.dom.Document;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TicketElectriqueActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuIcon;

    private Button button;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private int i=0,iA=0,iB=0,iC=0,iAa=0,iBb=0,iCc=0,iActuel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_electrique);

        menuIcon = findViewById(R.id.menu_ticket);
        drawerLayout = findViewById(R.id.drawer_layout_ticket);
        navigationView = findViewById(R.id.navigation_view_ticket);

        button = findViewById(R.id.btnTicket);
        radioGroup = findViewById(R.id.rgTiquet);


        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                int radio = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radio);

                String nameTicket = radioButton.getText().toString();

                try {
                    createPdf(nameTicket);
                }catch (FileNotFoundException e ){
                    e.printStackTrace();
                }

            }
        });


        navigationDrawer();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.devices:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(TicketElectriqueActivity.this, HomeScreen.class));
                        break;
                    case R.id.profile:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(TicketElectriqueActivity.this,ProfilActivity.class));
                        break;
                    case R.id.ticketE:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                }

                return true;
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void createPdf(String nameTicket) throws FileNotFoundException{
        i++;

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath , "TicketElectrique"+i+".pdf");
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument) {
        };

        pdfDocument.setDefaultPageSize(PageSize.A6);
        document.setMargins(5,5,5,5);

        Drawable d = getDrawable(R.drawable.img);
        Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] bitmapData = stream.toByteArray();

        ImageData imageData = ImageDataFactory.create(bitmapData);
        Image image = new Image(imageData);

        Paragraph title = new Paragraph("Ticket Electrique").setBold().setFontSize(20).setTextAlignment(TextAlignment.CENTER);
        Paragraph welcome = new Paragraph("Welcome").setBold().setFontSize(15).setTextAlignment(TextAlignment.CENTER);

        Paragraph nameTicketElectrique = null,numTicket=null;
        if (nameTicket.equals("Choix A")){
            iA++;
            nameTicketElectrique = new Paragraph("Choix A").setBold().setFontSize(12).setTextAlignment(TextAlignment.CENTER);
            numTicket = new Paragraph("A0"+iA).setBold().setFontSize(14).setTextAlignment(TextAlignment.CENTER);
            iActuel=iAa;
        }else if (nameTicket.equals("Choix B")){
            iB++;
            nameTicketElectrique = new Paragraph("Choix B").setTextAlignment(TextAlignment.CENTER).setBold().setFontSize(12);
            numTicket = new Paragraph("B0"+iB).setBold().setTextAlignment(TextAlignment.CENTER).setFontSize(14);
            iActuel=iBb;
        }else if (nameTicket.equals("Choix C")){
            iC++;
            nameTicketElectrique = new Paragraph("Choix C").setFontSize(12).setBold().setTextAlignment(TextAlignment.CENTER);
            numTicket = new Paragraph("C0"+iC).setFontSize(14).setBold().setTextAlignment(TextAlignment.CENTER);
            iActuel=iCc;
        }

        float[] width = {100f,100f};
        Table table = new Table(width);
        table.setHorizontalAlignment(HorizontalAlignment.CENTER);

        DateTimeFormatter dateTimeFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        }
        table.addCell(new Cell().add(new Paragraph("Date")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            table.addCell(new Cell().add(new Paragraph(LocalDate.now().format(dateTimeFormatter).toString())));
        }

        DateTimeFormatter timeFormatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        }
        table.addCell(new Cell().add(new Paragraph("Temps")));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            table.addCell(new Cell().add(new Paragraph(LocalTime.now().format(timeFormatter).toString())));
        }

        table.addCell(new Cell().add(new Paragraph("Numero Actuel")));
        table.addCell(new Cell().add(new Paragraph(String.valueOf(iActuel))));


        document.add(image);
        document.add(title);
        document.add(welcome);
        document.add(nameTicketElectrique);
        document.add(numTicket);
        document.add(table);

        document.close();

        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();


    }

    private void navigationDrawer() {
        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.ticketE);

        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START)){
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setScrimColor(getResources().getColor(R.color.colorApp));

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return true;
    }

}


