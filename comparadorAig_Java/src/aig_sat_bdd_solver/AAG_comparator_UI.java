package aig_sat_bdd_solver;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * @author Luciano/Rodrigo
 */

public class AAG_comparator_UI extends javax.swing.JFrame {

  
    public AAG_comparator_UI() {
       
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        j_aag1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        btnCompararSAT = new javax.swing.JButton();
        j_aag2 = new javax.swing.JTextField();
        btnCompararBDD = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Comparador AAGs");
        setName("frameLogin"); // NOI18N
        setResizable(false);

        j_aag1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("AAG1");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("AAG2");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Comparador AAGs");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        btnCompararSAT.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        btnCompararSAT.setText("Comparar via Sat");
        btnCompararSAT.setActionCommand("Comparar via SAT");
        btnCompararSAT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnCompararSATMousePressed(evt);
            }
        });

        j_aag2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        j_aag2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                j_aag2ActionPerformed(evt);
            }
        });

        btnCompararBDD.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        btnCompararBDD.setText("Comparar via BDD");
        btnCompararBDD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnCompararBDDMousePressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(jLabel3)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(65, 65, 65)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(j_aag2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(j_aag1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 28, Short.MAX_VALUE)
                        .addComponent(btnCompararSAT)
                        .addGap(27, 27, 27)
                        .addComponent(btnCompararBDD)
                        .addGap(60, 60, 60))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel3)
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(j_aag1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(j_aag2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCompararSAT)
                    .addComponent(btnCompararBDD))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnCompararBDDMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompararBDDMousePressed
        // TODO add your handling code here:
        btnCompararBDD.setEnabled(false);
     
            String arquivo1 = j_aag1.getText().toString();
       
            String arquivo2 = j_aag2.getText().toString();

        
        ComparadorAAGs comparador = new ComparadorAAGs();
        if(comparador.compararBdd(arquivo1, arquivo2)){
            JOptionPane.showMessageDialog(rootPane, "AAGs equivalentes", "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
        } else{
            JOptionPane.showMessageDialog(rootPane, "AAGs não equivalentes!", "Resultado", JOptionPane.ERROR_MESSAGE, null);
        }
        btnCompararBDD.setEnabled(true);
    }//GEN-LAST:event_btnCompararBDDMousePressed

    private void btnCompararSATMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCompararSATMousePressed
        // TODO add your handling code here:
        String arquivo1 = j_aag1.getText().toString();
        String arquivo2 = j_aag2.getText().toString();
        ComparadorAAGs comparador = new ComparadorAAGs();
        if(comparador.compararSat(arquivo1, arquivo2)){
            JOptionPane.showMessageDialog(rootPane, "AAGs equivalentes", "Resultado", JOptionPane.INFORMATION_MESSAGE, null);
        } else{
            JOptionPane.showMessageDialog(rootPane, "AAGs não equivalentes!", "Resultado", JOptionPane.ERROR_MESSAGE, null);
        }
    }//GEN-LAST:event_btnCompararSATMousePressed

    private void j_aag2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_j_aag2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_j_aag2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AAG_comparator_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AAG_comparator_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AAG_comparator_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AAG_comparator_UI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AAG_comparator_UI().setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCompararBDD;
    private javax.swing.JButton btnCompararSAT;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField j_aag1;
    private javax.swing.JTextField j_aag2;
    // End of variables declaration//GEN-END:variables
}
