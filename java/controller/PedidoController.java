package controller;

import model.Pedido;
import model.PedidoDAO;
import model.Cliente;
import model.ClienteDAO;
import model.Produto;
import model.ProdutoDAO;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet; 
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/pedidos") 
public class PedidoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getSession().getAttribute("usuarioLogado") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        PedidoDAO dao = new PedidoDAO();
        ClienteDAO cDao = new ClienteDAO();
        ProdutoDAO pDao = new ProdutoDAO();

        try {
            switch (action) {
                case "listar":
                    List<Pedido> pedidos = dao.listar();
                    req.setAttribute("pedidos", pedidos);
                    req.getRequestDispatcher("pedidos/list.jsp").forward(req, resp);
                    break;
                case "editar":
                    int id = req.getParameter("id") != null ? Integer.parseInt(req.getParameter("id")) : 0;
                    Pedido pedido = (id > 0) ? dao.getById(id) : new Pedido();
                    req.setAttribute("pedido", pedido);
                    req.setAttribute("clientes", cDao.listar());
                    req.setAttribute("produtos", pDao.listar());
                    req.getRequestDispatcher("pedidos/form.jsp").forward(req, resp);
                    break;
                case "deletar":
                    id = Integer.parseInt(req.getParameter("id"));
                    dao.deletar(id);
                    req.getSession().setAttribute("msg", "Pedido excluído com sucesso!");
                    resp.sendRedirect("pedidos?action=listar");
                    break;
                default:
                    resp.sendRedirect("pedidos?action=listar");
            }
        } catch (Exception e) {
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("pedidos?action=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (req.getSession().getAttribute("usuarioLogado") == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        try {
            Pedido p = new Pedido();
            p.setId(req.getParameter("id") != null && !req.getParameter("id").isEmpty() ?
                    Integer.parseInt(req.getParameter("id")) : 0);
            p.setClienteId(Integer.parseInt(req.getParameter("cliente_id")));

            Map<Integer, Integer> produtos = new java.util.HashMap<>();
            String[] produtosId = req.getParameterValues("produto_id");
            String[] quantidades = req.getParameterValues("quantidade");

            if (produtosId != null && quantidades != null) {
                for (int i = 0; i < produtosId.length; i++) {
                    produtos.put(Integer.parseInt(produtosId[i]), Integer.parseInt(quantidades[i]));
                }
            }

            p.setProdutos(produtos);

            PedidoDAO dao = new PedidoDAO();
            if (p.getId() == 0) {
                dao.salvar(p);
                req.getSession().setAttribute("msg", "Pedido cadastrado com sucesso!");
            } else {
                dao.atualizar(p);
                req.getSession().setAttribute("msg", "Pedido atualizado com sucesso!");
            }

            resp.sendRedirect("pedidos?action=listar");

        } catch (Exception e) {
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("pedidos?action=listar");
        }
    }
}
