package controller;

import model.Produto;
import model.ProdutoDAO;
import model.Categoria;
import model.CategoriaDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/produtos")
public class ProdutoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if(req.getSession().getAttribute("usuarioLogado") == null){
            resp.sendRedirect("login.jsp");
            return;
        }

        String action = req.getParameter("action");
        ProdutoDAO dao = new ProdutoDAO();
        CategoriaDAO catDao = new CategoriaDAO();

        try {
            if(action == null || action.equals("listar")){
                List<Produto> produtos = dao.listar();
                req.setAttribute("produtos", produtos);

                String msg = (String) req.getSession().getAttribute("msg");
                if(msg != null){
                    req.setAttribute("msg", msg);
                    req.getSession().removeAttribute("msg");
                }

                req.getRequestDispatcher("/produtos/list.jsp").forward(req, resp);

            } else if(action.equals("editar")){
                String idStr = req.getParameter("id");
                Produto p = null;
                if(idStr != null && !idStr.isEmpty()){
                    p = dao.getProdutoById(Integer.parseInt(idStr));
                }
                List<Categoria> categorias = catDao.listar();
                req.setAttribute("produto", p);
                req.setAttribute("categorias", categorias);
                req.getRequestDispatcher("/produtos/form.jsp").forward(req, resp);

            } else if(action.equals("deletar")){
                int id = Integer.parseInt(req.getParameter("id"));
                dao.deletar(id);
                req.getSession().setAttribute("msg", "Produto excluído com sucesso!");
                resp.sendRedirect("produtos?action=listar");
            }

        } catch (Exception e){
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("produtos?action=listar");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Garante que os parâmetros serão lidos como UTF-8
        req.setCharacterEncoding("UTF-8");

        // Verifica se o usuário está logado
        if(req.getSession().getAttribute("usuarioLogado") == null){
            resp.sendRedirect("login.jsp");
            return;
        }

        // Cria o objeto Produto
        Produto p = new Produto();
        
        String idParam = req.getParameter("id");
        p.setId(idParam != null && !idParam.trim().isEmpty() ?
                Integer.parseInt(idParam.trim()) : 0);

        String nome = req.getParameter("nome");
        p.setNome(nome != null ? nome.trim() : "");

        String precoParam = req.getParameter("preco");
        p.setPreco(precoParam != null && !precoParam.trim().isEmpty() ?
                Double.parseDouble(precoParam.trim()) : 0.0);

        String estoqueParam = req.getParameter("estoque");
        p.setEstoque(estoqueParam != null && !estoqueParam.trim().isEmpty() ?
                Integer.parseInt(estoqueParam.trim()) : 0);

        String categoriaParam = req.getParameter("categoria_id");
        p.setCategoriaId(categoriaParam != null && !categoriaParam.trim().isEmpty() ?
                Integer.parseInt(categoriaParam.trim()) : 0);

        ProdutoDAO dao = new ProdutoDAO();

        try {
            if(p.getId() == 0){
                dao.salvar(p);
                req.getSession().setAttribute("msg", "Produto cadastrado com sucesso!");
            } else {
                dao.atualizar(p);
                req.getSession().setAttribute("msg", "Produto atualizado com sucesso!");
            }
            resp.sendRedirect("produtos?action=listar");
        } catch (Exception e){
            req.getSession().setAttribute("msg", "Erro: " + e.getMessage());
            resp.sendRedirect("produtos?action=listar");
        }
    }

}
