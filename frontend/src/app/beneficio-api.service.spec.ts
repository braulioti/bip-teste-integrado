import { HttpErrorResponse, provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { BeneficioApiService } from './beneficio-api.service';

describe('BeneficioApiService', () => {
  const runtimeWindow = globalThis as typeof globalThis & {
    __env?: { API_BASE_URL?: string };
  };

  let httpTesting: HttpTestingController;

  afterEach(() => {
    httpTesting?.verify();
    delete runtimeWindow.__env;
    TestBed.resetTestingModule();
  });

  it('uses the runtime API base URL when available', () => {
    runtimeWindow.__env = { API_BASE_URL: '/custom/api/beneficios' };

    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    const service = TestBed.inject(BeneficioApiService);
    httpTesting = TestBed.inject(HttpTestingController);

    service.list().subscribe();

    httpTesting.expectOne('/custom/api/beneficios').flush([]);
  });

  it('falls back to the default API base URL', () => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    const service = TestBed.inject(BeneficioApiService);
    httpTesting = TestBed.inject(HttpTestingController);

    service.list().subscribe();

    httpTesting.expectOne('/api/v1/beneficios').flush([]);
  });

  it('extracts the backend message from HTTP errors', () => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });

    const service = TestBed.inject(BeneficioApiService);
    httpTesting = TestBed.inject(HttpTestingController);

    const message = service.getErrorMessage(
      new HttpErrorResponse({
        error: { message: 'Falha ao cadastrar beneficio.' },
        status: 400,
      }),
    );

    expect(message).toBe('Falha ao cadastrar beneficio.');
  });
});
