import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';

export interface Beneficio {
  id: number;
  nome: string;
  descricao: string | null;
  valor: number;
  ativo: boolean;
  version: number;
}

export interface BeneficioPayload {
  nome: string;
  descricao: string | null;
  valor: number;
  ativo: boolean;
}

export interface BeneficioTransferPayload {
  beneficioOrigemId: number;
  beneficioDestinoId: number;
  valor: number;
}

export interface BeneficioTransferResponse {
  beneficioOrigem: Beneficio;
  beneficioDestino: Beneficio;
  valorTransferido: number;
}

interface ApiErrorResponse {
  message?: string;
}

interface FrontendRuntimeConfig {
  API_BASE_URL?: string;
}

@Injectable({ providedIn: 'root' })
export class BeneficioApiService {
  private readonly http = inject(HttpClient);
  private readonly apiBaseUrl = this.resolveApiBaseUrl();
  private readonly transferUrl = `${this.apiBaseUrl}/transferencias`;

  list(): Observable<Beneficio[]> {
    return this.http.get<Beneficio[]>(this.apiBaseUrl);
  }

  create(payload: BeneficioPayload): Observable<Beneficio> {
    return this.http.post<Beneficio>(this.apiBaseUrl, payload);
  }

  update(id: number, payload: BeneficioPayload): Observable<Beneficio> {
    return this.http.put<Beneficio>(`${this.apiBaseUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiBaseUrl}/${id}`);
  }

  transfer(payload: BeneficioTransferPayload): Observable<BeneficioTransferResponse> {
    return this.http.post<BeneficioTransferResponse>(this.transferUrl, payload);
  }

  getErrorMessage(error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      const apiError = error.error as ApiErrorResponse | null;
      return apiError?.message || error.message || 'Nao foi possivel concluir a operacao.';
    }

    return 'Nao foi possivel concluir a operacao.';
  }

  private resolveApiBaseUrl(): string {
    const runtimeWindow = globalThis as typeof globalThis & {
      __env?: FrontendRuntimeConfig;
    };
    const configuredBaseUrl = runtimeWindow.__env?.API_BASE_URL?.trim();

    if (!configuredBaseUrl) {
      return '/api/v1/beneficios';
    }

    return this.normalizeApiBaseUrl(configuredBaseUrl);
  }

  private normalizeApiBaseUrl(baseUrl: string): string {
    if (baseUrl.startsWith('/')) {
      return baseUrl === '/' ? '/api/v1/beneficios' : baseUrl.replace(/\/+$/, '');
    }

    try {
      const parsedUrl = new URL(baseUrl);
      const normalizedPath =
        parsedUrl.pathname === '/' ? '/api/v1/beneficios' : parsedUrl.pathname.replace(/\/+$/, '');

      return `${parsedUrl.origin}${normalizedPath}`;
    } catch {
      return baseUrl.replace(/\/+$/, '') || '/api/v1/beneficios';
    }
  }
}
