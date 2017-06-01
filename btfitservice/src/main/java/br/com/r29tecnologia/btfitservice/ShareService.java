package br.com.r29tecnologia.btfitservice;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/share")
public class ShareService {

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Retorno formata(List<Dia> dias) {
		StringBuilder build = new StringBuilder();
		build.append("<html><table border='1px' cellspacing='0'><tr><td>Dia</td><td>Dieta</td><td>Atv. Física</td><td>Observação</td><tr>");
		String linhaFormat = "<tr><td>%s</td><td>%d</td><td>%d</td><td>%s</td></tr></html>";
		if (dias != null) {
			for (Dia dia : dias) {
				build.append(String.format(linhaFormat, dia.date.toString(), dia.flagDieta, dia.flagAtvFisica, dia.observacao));
			}
		}
		build.append("</table>");
		Retorno r = new Retorno();
		r.corpo = build.toString();
		return r;
	}

	@GET
	@Path("/gera")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Dia> gera() {
		List<Dia> dias = new ArrayList<>();
		Dia dia = new Dia();
		dia.date = new Date();
		dia.flagAtvFisica = 1;
		dia.flagDieta = 2;
		dia.observacao = "obser";
		dia.peso = 10f;
		dia.preenchido = false;
		dias.add(dia);
		return dias;

	}

	public static class Retorno {

		private String corpo;

		public String getCorpo() {
			return corpo;
		}

		public void setCorpo(String corpo) {
			this.corpo = corpo;
		}

	}

	public static class Dia {

		private boolean preenchido;
		private Date date;
		private int flagDieta;
		private int flagAtvFisica;
		private String observacao;
		private Float peso;

		public boolean isPreenchido() {
			return preenchido;
		}

		public void setPreenchido(boolean preenchido) {
			this.preenchido = preenchido;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public int getFlagDieta() {
			return flagDieta;
		}

		public void setFlagDieta(int flagDieta) {
			this.flagDieta = flagDieta;
		}

		public int getFlagAtvFisica() {
			return flagAtvFisica;
		}

		public void setFlagAtvFisica(int flagAtvFisica) {
			this.flagAtvFisica = flagAtvFisica;
		}

		public String getObservacao() {
			return observacao;
		}

		public void setObservacao(String observacao) {
			this.observacao = observacao;
		}

		public Float getPeso() {
			return peso;
		}

		public void setPeso(Float peso) {
			this.peso = peso;
		}

	}

}
