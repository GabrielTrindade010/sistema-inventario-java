package view;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Painel para consulta de usuários
 */
public class ConsultaUsuarioPanel extends JPanel {

    private JTextField txtBusca;
    private JButton btnBuscar;
    private JButton btnTodos;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnEditar;
    private JButton btnExcluir;
    private JButton btnAtivarDesativar;

    private UsuarioController usuarioController;
    private DashboardView dashboardView;

    public ConsultaUsuarioPanel(DashboardView dashboardView) {
        this.dashboardView = dashboardView;
        this.usuarioController = new UsuarioController();
        initComponents();
        carregarTodosUsuarios();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Painel de título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setPreferredSize(new Dimension(750, 60));

        JLabel lblTitle = new JLabel("Consulta de Usuários");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        titlePanel.add(lblTitle);

        add(titlePanel, BorderLayout.NORTH);

        // Painel de busca
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblBusca = new JLabel("Buscar por nome:");
        lblBusca.setFont(new Font("Arial", Font.BOLD, 14));
        searchPanel.add(lblBusca);

        txtBusca = new JTextField(30);
        txtBusca.setFont(new Font("Arial", Font.PLAIN, 14));
        searchPanel.add(txtBusca);

        btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Arial", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(52, 152, 219));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> buscarUsuarios());
        searchPanel.add(btnBuscar);

        btnTodos = new JButton("Todos");
        btnTodos.setFont(new Font("Arial", Font.BOLD, 14));
        btnTodos.setBackground(new Color(149, 165, 166));
        btnTodos.setForeground(Color.WHITE);
        btnTodos.setFocusPainted(false);
        btnTodos.addActionListener(e -> carregarTodosUsuarios());
        searchPanel.add(btnTodos);

        add(searchPanel, BorderLayout.NORTH);

        // Painel da tabela
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] colunas = {"ID", "Nome", "Login", "Tipo", "Status"};
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnEditar = new JButton("Editar");
        btnEditar.setFont(new Font("Arial", Font.BOLD, 14));
        btnEditar.setBackground(new Color(241, 196, 15));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setPreferredSize(new Dimension(140, 35));
        btnEditar.addActionListener(e -> editarUsuario());
        buttonPanel.add(btnEditar);

        btnAtivarDesativar = new JButton("Ativar/Desativar");
        btnAtivarDesativar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAtivarDesativar.setBackground(new Color(52, 152, 219));
        btnAtivarDesativar.setForeground(Color.WHITE);
        btnAtivarDesativar.setFocusPainted(false);
        btnAtivarDesativar.setPreferredSize(new Dimension(140, 35));
        btnAtivarDesativar.addActionListener(e -> alterarStatusUsuario());
        buttonPanel.add(btnAtivarDesativar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setFont(new Font("Arial", Font.BOLD, 14));
        btnExcluir.setBackground(new Color(231, 76, 60));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setPreferredSize(new Dimension(140, 35));
        btnExcluir.addActionListener(e -> excluirUsuario());
        buttonPanel.add(btnExcluir);

        add(buttonPanel, BorderLayout.SOUTH);

        // Enter para buscar
        txtBusca.addActionListener(e -> buscarUsuarios());
    }

    private void carregarTodosUsuarios() {
        try {
            List<Usuario> usuarios = usuarioController.listarTodos();
            atualizarTabela(usuarios);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar usuários: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarUsuarios() {
        try {
            String busca = txtBusca.getText().trim();
            List<Usuario> usuarios;

            if (busca.isEmpty()) {
                usuarios = usuarioController.listarTodos();
            } else {
                usuarios = usuarioController.buscarPorNome(busca);
            }

            atualizarTabela(usuarios);

            if (usuarios.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Nenhum usuário encontrado",
                        "Informação",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao buscar usuários: " + ex.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarTabela(List<Usuario> usuarios) {
        tableModel.setRowCount(0);

        for (Usuario usuario : usuarios) {
            Object[] linha = {
                    usuario.getId(),
                    usuario.getNome(),
                    usuario.getLogin(),
                    usuario.getTipoUsuario().getValor(),
                    usuario.isAtivo() ? "Ativo" : "Inativo"
            };
            tableModel.addRow(linha);
        }
    }

    private void editarUsuario() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para editar",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        Usuario usuario = usuarioController.buscarPorId(id);

        if (usuario != null) {
            // Abrir painel de edição
            JPanel contentPanel = (JPanel) dashboardView.getContentPane().getComponent(2);
            contentPanel.removeAll();
            CadastroUsuarioPanel panel = new CadastroUsuarioPanel(dashboardView, usuario);
            contentPanel.add(panel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        }
    }

    private void alterarStatusUsuario() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para ativar/desativar",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nome = (String) tableModel.getValueAt(selectedRow, 1);
        String statusAtual = (String) tableModel.getValueAt(selectedRow, 4);

        boolean ativoAtual = statusAtual.equals("Ativo");
        String novoStatus = ativoAtual ? "inativar" : "ativar";

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente " + novoStatus + " o usuário:\n" + nome + "?",
                "Confirmar Alteração",
                JOptionPane.YES_NO_OPTION);

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = usuarioController.alterarStatus(id, !ativoAtual);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Status do usuário alterado com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);

                    carregarTodosUsuarios();
                    dashboardView.refreshDashboard();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao alterar status do usuário",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao alterar status: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluirUsuario() {
        int selectedRow = table.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um usuário para excluir",
                    "Atenção",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Integer id = (Integer) tableModel.getValueAt(selectedRow, 0);
        String nome = (String) tableModel.getValueAt(selectedRow, 1);

        int opcao = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir o usuário:\n" + nome + "?\n\n" +
                        "ATENÇÃO: Esta ação não pode ser desfeita!",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (opcao == JOptionPane.YES_OPTION) {
            try {
                boolean sucesso = usuarioController.excluirUsuario(id);

                if (sucesso) {
                    JOptionPane.showMessageDialog(this,
                            "Usuário excluído com sucesso!",
                            "Sucesso",
                            JOptionPane.INFORMATION_MESSAGE);

                    carregarTodosUsuarios();
                    dashboardView.refreshDashboard();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao excluir usuário",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir usuário: " + ex.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}