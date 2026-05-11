import { provideHttpClient } from '@angular/common/http';
import {
  HttpTestingController,
  provideHttpClientTesting,
} from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { App } from './app';

describe('App', () => {
  let httpTesting: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideHttpClient(), provideHttpClientTesting()],
    }).compileComponents();

    httpTesting = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpTesting.verify();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    httpTesting.expectOne('/api/v1/beneficios').flush([]);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should render title', async () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    httpTesting.expectOne('/api/v1/beneficios').flush([]);
    await fixture.whenStable();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.hero-brand h1')?.textContent).toContain('Bip Brasil');
    expect(compiled.querySelector('.hero-subtitle')?.textContent).toContain('Gestao de Beneficios');
  });

  it('should render beneficios returned by backend', async () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    httpTesting.expectOne('/api/v1/beneficios').flush([
      {
        id: 1,
        nome: 'Beneficio A',
        descricao: 'Descricao A',
        valor: 1000,
        ativo: true,
        version: 0,
      },
      {
        id: 2,
        nome: 'Beneficio B',
        descricao: 'Descricao B',
        valor: 600,
        ativo: false,
        version: 0,
      },
    ]);
    await fixture.whenStable();
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const cards = compiled.querySelectorAll('.beneficio-card');
    expect(cards).toHaveLength(2);
    expect(compiled.querySelector('.beneficio-title-row h3')?.textContent).toContain(
      'Beneficio A',
    );
    expect(compiled.querySelector('.status-pill')?.textContent).toContain('Ativo');
  });

  it('should close delete modal when creating a new beneficio', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    httpTesting.expectOne('/api/v1/beneficios').flush([]);

    const app = fixture.componentInstance;
    app.deletingBeneficio = {
      id: 99,
      nome: 'Beneficio antigo',
      descricao: null,
      valor: 10,
      ativo: true,
      version: 0,
    };

    app.createForm.setValue({
      nome: 'Vale alimentacao',
      descricao: 'Novo beneficio',
      valor: 100,
      ativo: true,
    });

    app.createBeneficio();

    const request = httpTesting.expectOne('/api/v1/beneficios');
    expect(request.request.method).toBe('POST');
    request.flush({
      id: 1,
      nome: 'Vale alimentacao',
      descricao: 'Novo beneficio',
      valor: 100,
      ativo: true,
      version: 0,
    });

    expect(app.deletingBeneficio).toBeNull();
  });
});
