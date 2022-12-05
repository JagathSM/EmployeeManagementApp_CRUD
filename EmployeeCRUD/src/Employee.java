import net.proteanit.sql.DbUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.*;

public class Employee {
    private JPanel Main;
    private JTextField txtName;
    private JTextField txtSalary;
    private JTextField txtMobile;
    private JButton saveButton;
    private JTable table1;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField txtid;
    private JScrollPane table_1; // inside 'JTable' we have 'JScrollPane'.

    public static void main(String[] args) {
        JFrame frame = new JFrame("Employee");
        frame.setContentPane(new Employee().Main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    Connection con;
    PreparedStatement pst;

    public void connect() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3307/rbcompany", "root", "");
            System.out.println("Success"); // To check connection working
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
        catch(SQLException e){

        }
    }

    public void table_load(){
        try
        {
            pst = con.prepareStatement("select * from employee");
            ResultSet rs = pst.executeQuery();
            table1.setModel(DbUtils.resultSetToTableModel(rs));
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }


    public Employee() {
        connect(); // database connect
        table_load(); // load the table
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String empname, salary, mobile;

                empname = txtName.getText();
                salary = txtSalary.getText();
                mobile = txtMobile.getText();


                try {
                    pst = con.prepareStatement("insert into employee(empname,salary,mobile)values(?,?,?)");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Addedddd!!!!!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus(); // After setting all, it will come to txtName to set name.
                }

                catch (SQLException e1)
                {

                    e1.printStackTrace();
                }

            }
        });

        // Search button
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try
                {
                    String empid = txtid.getText();

                    pst = con.prepareStatement("select empname,salary,mobile from employee where id = ?");
                    pst.setString(1, empid);
                    ResultSet rs = pst.executeQuery();

                    // If record is available, all the things passing through the relevant text fields.
                    if(rs.next()==true)
                    {
                        String empname = rs.getString(1);
                        String emsalary = rs.getString(2);
                        String emmobile = rs.getString(3);

                        txtName.setText(empname);
                        txtSalary.setText(emsalary);
                        txtMobile.setText(emmobile);

                    }
                    else
                    {
                        txtName.setText("");
                        txtSalary.setText("");
                        txtMobile.setText("");
                        JOptionPane.showMessageDialog(null,"Invalid Employee Number");

                    }
                }
                catch (SQLException ex)
                {
                    ex.printStackTrace();
                }


            }
        });

        // Update button : search using id, then edit to change the Employee details.
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empname, salary, mobile, empid;

                empname = txtName.getText();
                salary = txtSalary.getText();
                mobile = txtMobile.getText();
                empid = txtid.getText();

                try {
                    pst = con.prepareStatement("update employee set empname = ?,salary = ?,mobile = ? where id = ?");
                    pst.setString(1, empname);
                    pst.setString(2, salary);
                    pst.setString(3, mobile);
                    pst.setString(4, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Updated!!!!!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {
                    e1.printStackTrace();
                }
            }

        });

        // Delete button : enter id, then search and click delete button to delete the id corresponding record.
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String empid;
                empid = txtid.getText();

                try {
                    pst = con.prepareStatement("delete from employee  where id = ?");

                    pst.setString(1, empid);

                    pst.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Record Deleted!!!!!");
                    table_load();
                    txtName.setText("");
                    txtSalary.setText("");
                    txtMobile.setText("");
                    txtName.requestFocus();
                }

                catch (SQLException e1)
                {

                    e1.printStackTrace();
                }
            }

        });
    }



}






