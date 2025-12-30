package view;

import util.BackupUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Painel para gerenciamento de backups do banco de dados
 */
public class BackupPanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnCriarBackup;
    private JButton btnRestaurar;
    private JButton btnExcluir;
    private JButton btnAtualizar;

    public BackupPanel() {
        initComponents();
        carregarBackups();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Painel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setPreferredSize(new Dimension(750, 60));

        JLabel lblTitle = new JLabel("Backup e Restauração do Banco de Dados");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);

        add(titlePanel, BorderLayout.NORTH);

        // Painel de informações
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(236, 240, 241));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblInfo1 = new JLabel("ℹ️ Criar Backup: Gera uma cópia completa do banco de dados");
        lblInfo1.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(lblInfo1);

        infoPanel.add(Box.createVerticalStrut(5));

        JLabel lblInfo2 = new JLabel("ℹ️ Restaurar: Substitui o banco atual pelo backup selecionado (USE COM CUIDADO!)");
        lblInfo2.setFont(new Font("Arial", Font.PLAIN, 13));
        lblInfo2.setForeground(new Color(192, 57, 43));
        infoPanel.add(lblInfo2);

        infoPanel.add(Box.createVerticalStrut(5));

        JLabel lblInfo3 = new JLabel("ℹ️ Excluir: Remove permanentemente o arquivo de backup selecionado");
        lblInfo3.setFont(new Font("Arial", Font.PLAIN, 13));
        infoPanel.add(lblInfo3);

        add(infoPanel, BorderLayout.NORTH);

        // Painel da tabela
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel lblListaBackups = new JLabel("Lista de Backups Disponíveis:");
        lblListaBackups.setFont(new Font("Arial", Font.BOLD, 16));
        lblListaBackups.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        tablePanel.add(lblListaBackups, BorderLayout.NORTH);

        String[] colunas = {"Nome do Arquivo", "Data de Criação", "Tamanho (KB)"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(Color.WHITE);

        btnCriarBackup = new JButton("Criar Backup");
        btnCriarBackup.setFont(new Font("Arial", Font.BOLD, 14));
        btnCriarBackup.setBackground(new Color(46, 204, 113));
        btnCriarBackup.setForeground(Color.WHITE);
        btnCriarBackup.setFocusPainted(false);
        btnCriarBackup.setPreferredSize(new Dimension(150, 40));
        btnCriarBackup.addActionListener(e -> criarBackup());
        buttonPanel.add(btnCriarBackup);

        btnRestaurar = new JButton("Restaurar");
        btnRestaurar.setFont(new Font("Arial", Font.BOLD, 14));
        btnRestaurar.setBackground(new Color(52, 152, 219));
        btnRestaurar.setForeground(Color.WHITE);
        btnRestaurar.setFocusPainted(false);
        btnRestaurar.setPreferredSize(new Dimension(150, 40));
        btnRestaurar.addActionListener(e -> restaurarBackup());
        buttonPanel.add(btnRestaurar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setFont(new Font("Arial", Font.BOLD, 14));
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setPreferredSize(new Dimension(150, 40));
        btnExcluir.addActionListener(e -> excluirBackup());
        buttonPanel.add(btnExcluir);

        btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAtualizar.setBackground(new Color(149, 165, 166));
        btnAtualizar.setForeground(Color.WHITE);
        btnAtualizar.setFocusPainted(false);
        btnAtualizar.setPreferredSize(new Dimension(150, 40));
        btnAtualizar.addActionListener(e -> carregarBackups());
        buttonPanel.add(btnAtualizar);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void carregarBackups() {
        try {
            List<File> backups = BackupUtil.listBackups();
            tableModel.setRowCount(0);

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            for (File backup : backups) {
                String nome = backup.getName();
                String data = sdf.format(new Date(backup.lastModified()));
                long tamanhoKB = backup.length() / 1024;

                Object[] linha = {nome, data, tamanhoKB};
                tableModel.addRow(linha);
            }

            if (backups.isEmpty()) {
                JLabel lblSemBackups = new JLabel("Nenhum backup disponível");
                lblSemBackups.setFont(new Font("Arial", Font.ITALIC, 14));
                lblSemBackups.setForeground(Color.GRAY);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar lista de backups: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void criarBackup() {
        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja criar um backup do banco de dados?\n" +
                        "Esta operação pode levar alguns instantes.",
                "Confirmar Backup",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            // Mostrar progresso
            JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                    "Criando Backup...", true);
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressDialog.add(progressBar);
            progressDialog.setSize(300, 80);
            progressDialog.setLocationRelativeTo(this);

            SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
                @Override
                protected String doInBackground() throws Exception {
                    return BackupUtil.createBackup();
                }

                @Override
                protected void done() {
                    progressDialog.dispose();
                    try {
                        String filename = get();
                        JOptionPane.showMessageDialog(BackupPanel.this,
                                "Backup criado com sucesso!\n" +
                                        "Arquivo: " + filename,
                                "Sucesso",
                                JOptionPane.INFORMATION_MESSAGE);
                        carregarBackups();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(BackupPanel.this,
                                "Erro ao criar backup:\n" + ex.getMessage(),
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
            progressDialog.setVisible(true);
        }
    }

    private void restaurarBackup() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um backup para restaurar",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeArquivo = (String) tableModel.getValueAt(selectedRow, 0);

        int opcao = JOptionPane.showConfirmDialog(this,
                "⚠️ ATENÇÃO: Esta operação irá substituir TODOS os dados atuais!\n\n" +
                        "Backup selecionado: " + nomeArquivo + "\n\n" +
                        "Deseja realmente continuar?",
                "Confirmar Restauração",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcao == JOptionPane.YES_OPTION) {
            // Segunda confirmação
            int segundaConfirmacao = JOptionPane.showConfirmDialog(this,
                    "Tem certeza ABSOLUTA?\n" +
                            "Os dados atuais serão PERDIDOS!",
                    "Última Confirmação",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (segundaConfirmacao == JOptionPane.YES_OPTION) {
                File backupFile = new File("backups/" + nomeArquivo);

                // Mostrar progresso
                JDialog progressDialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                        "Restaurando Backup...", true);
                JProgressBar progressBar = new JProgressBar();
                progressBar.setIndeterminate(true);
                progressDialog.add(progressBar);
                progressDialog.setSize(300, 80);
                progressDialog.setLocationRelativeTo(this);

                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        BackupUtil.restoreBackup(backupFile);
                        return null;
                    }

                    @Override
                    protected void done() {
                        progressDialog.dispose();
                        try {
                            get();
                            JOptionPane.showMessageDialog(BackupPanel.this,
                                    "Backup restaurado com sucesso!\n" +
                                            "Reinicie a aplicação para garantir consistência.",
                                    "Sucesso",
                                    JOptionPane.INFORMATION_MESSAGE);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(BackupPanel.this,
                                    "Erro ao restaurar backup:\n" + ex.getMessage(),
                                    "Erro",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };

                worker.execute();
                progressDialog.setVisible(true);
            }
        }
    }

    private void excluirBackup() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um backup para excluir",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nomeArquivo = (String) tableModel.getValueAt(selectedRow, 0);

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o backup:\n" + nomeArquivo + "?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                File backupFile = new File("backups/" + nomeArquivo);
                boolean sucesso = BackupUtil.deleteBackup(backupFile);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Backup excluído com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);
                    carregarBackups();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir backup",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir backup: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}