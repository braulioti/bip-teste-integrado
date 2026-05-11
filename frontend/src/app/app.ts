import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { finalize } from 'rxjs';
import {
  Beneficio,
  BeneficioApiService,
  BeneficioPayload,
  BeneficioTransferPayload,
  BeneficioTransferResponse,
} from './beneficio-api.service';

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly beneficioApi = inject(BeneficioApiService);

  readonly title = 'Gestao de Beneficios';

  readonly createForm = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(100)]],
    descricao: [''],
    valor: [0, [Validators.required, Validators.min(0)]],
    ativo: [true, [Validators.required]],
  });

  readonly editForm = this.fb.nonNullable.group({
    nome: ['', [Validators.required, Validators.maxLength(100)]],
    descricao: [''],
    valor: [0, [Validators.required, Validators.min(0)]],
    ativo: [true, [Validators.required]],
  });

  readonly transferForm = this.fb.nonNullable.group({
    beneficioDestinoId: [0, [Validators.required, Validators.min(1)]],
    valor: [0, [Validators.required, Validators.min(0.01)]],
  });

  beneficios: Beneficio[] = [];
  loading = false;
  createSubmitting = false;
  editSubmitting = false;
  deleteSubmitting = false;
  transferSubmitting = false;
  feedback: { type: 'success' | 'error'; message: string } | null = null;
  editingBeneficio: Beneficio | null = null;
  deletingBeneficio: Beneficio | null = null;
  transferringBeneficio: Beneficio | null = null;

  ngOnInit(): void {
    this.loadBeneficios();
  }

  loadBeneficios(): void {
    this.loading = true;

    this.beneficioApi
      .list()
      .pipe(finalize(() => (this.loading = false)))
      .subscribe({
        next: (beneficios) => {
          this.beneficios = [...beneficios].sort((a, b) => a.id - b.id);
        },
        error: (error) => {
          this.showError(this.beneficioApi.getErrorMessage(error));
        },
      });
  }

  createBeneficio(): void {
    if (this.createForm.invalid) {
      this.createForm.markAllAsTouched();
      return;
    }

    this.createSubmitting = true;
    this.feedback = null;

    this.beneficioApi
      .create(this.buildBeneficioPayload(this.createForm.getRawValue()))
      .pipe(finalize(() => (this.createSubmitting = false)))
      .subscribe({
        next: (beneficio) => {
          this.upsertBeneficio(beneficio);
          this.createForm.reset({
            nome: '',
            descricao: '',
            valor: 0,
            ativo: true,
          });
          this.showSuccess('Beneficio cadastrado com sucesso.');
        },
        error: (error) => {
          this.showError(this.beneficioApi.getErrorMessage(error));
        },
      });
  }

  openEditModal(beneficio: Beneficio): void {
    this.editingBeneficio = beneficio;
    this.editForm.reset({
      nome: beneficio.nome,
      descricao: beneficio.descricao ?? '',
      valor: beneficio.valor,
      ativo: beneficio.ativo,
    });
  }

  closeEditModal(): void {
    this.editingBeneficio = null;
    this.editForm.reset({
      nome: '',
      descricao: '',
      valor: 0,
      ativo: true,
    });
  }

  updateBeneficio(): void {
    if (!this.editingBeneficio) {
      return;
    }

    if (this.editForm.invalid) {
      this.editForm.markAllAsTouched();
      return;
    }

    this.editSubmitting = true;
    this.feedback = null;

    this.beneficioApi
      .update(
        this.editingBeneficio.id,
        this.buildBeneficioPayload(this.editForm.getRawValue()),
      )
      .pipe(finalize(() => (this.editSubmitting = false)))
      .subscribe({
        next: (beneficio) => {
          this.upsertBeneficio(beneficio);
          this.closeEditModal();
          this.showSuccess('Beneficio atualizado com sucesso.');
        },
        error: (error) => {
          this.showError(this.beneficioApi.getErrorMessage(error));
        },
      });
  }

  openDeleteModal(beneficio: Beneficio): void {
    this.deletingBeneficio = beneficio;
  }

  closeDeleteModal(): void {
    this.deletingBeneficio = null;
  }

  deleteBeneficio(): void {
    if (!this.deletingBeneficio) {
      return;
    }

    const beneficioId = this.deletingBeneficio.id;
    const beneficioNome = this.deletingBeneficio.nome;

    this.deleteSubmitting = true;
    this.feedback = null;

    this.beneficioApi
      .delete(beneficioId)
      .pipe(finalize(() => (this.deleteSubmitting = false)))
      .subscribe({
        next: () => {
          this.beneficios = this.beneficios.filter(({ id }) => id !== beneficioId);
          this.closeDeleteModal();
          this.showSuccess(`Beneficio "${beneficioNome}" excluido com sucesso.`);
        },
        error: (error) => {
          this.showError(this.beneficioApi.getErrorMessage(error));
        },
      });
  }

  openTransferModal(beneficio: Beneficio): void {
    this.transferringBeneficio = beneficio;
    this.transferForm.reset({
      beneficioDestinoId: 0,
      valor: 0,
    });
  }

  closeTransferModal(): void {
    this.transferringBeneficio = null;
    this.transferForm.reset({
      beneficioDestinoId: 0,
      valor: 0,
    });
  }

  transferBeneficio(): void {
    if (!this.transferringBeneficio) {
      return;
    }

    if (this.transferForm.invalid) {
      this.transferForm.markAllAsTouched();
      return;
    }

    const payload: BeneficioTransferPayload = {
      beneficioOrigemId: this.transferringBeneficio.id,
      beneficioDestinoId: Number(this.transferForm.getRawValue().beneficioDestinoId),
      valor: Number(this.transferForm.getRawValue().valor),
    };

    this.transferSubmitting = true;
    this.feedback = null;

    this.beneficioApi
      .transfer(payload)
      .pipe(finalize(() => (this.transferSubmitting = false)))
      .subscribe({
        next: (response) => {
          this.applyTransferResponse(response);
          this.closeTransferModal();
          this.showSuccess(
            `Transferencia de ${this.formatCurrency(response.valorTransferido)} realizada com sucesso.`,
          );
        },
        error: (error) => {
          this.showError(this.beneficioApi.getErrorMessage(error));
        },
      });
  }

  get transferTargets(): Beneficio[] {
    if (!this.transferringBeneficio) {
      return [];
    }

    return this.beneficios.filter(
      (beneficio) => beneficio.id !== this.transferringBeneficio?.id,
    );
  }

  fieldError(form: FormGroup, fieldName: string): string {
    const control = form.get(fieldName);

    if (!control || !control.touched || !control.errors) {
      return '';
    }

    if (control.errors['required']) {
      return 'Campo obrigatorio.';
    }

    if (control.errors['maxlength']) {
      return 'O texto informado esta maior do que o permitido.';
    }

    if (control.errors['min']) {
      return 'Informe um valor valido.';
    }

    return 'Campo invalido.';
  }

  formatCurrency(value: number): string {
    return new Intl.NumberFormat('pt-BR', {
      style: 'currency',
      currency: 'BRL',
    }).format(value);
  }

  dismissFeedback(): void {
    this.feedback = null;
  }

  private buildBeneficioPayload(formValue: {
    nome: string;
    descricao: string;
    valor: number;
    ativo: boolean;
  }): BeneficioPayload {
    return {
      nome: formValue.nome.trim(),
      descricao: formValue.descricao.trim() || null,
      valor: Number(formValue.valor),
      ativo: formValue.ativo,
    };
  }

  private applyTransferResponse(response: BeneficioTransferResponse): void {
    this.upsertBeneficio(response.beneficioOrigem);
    this.upsertBeneficio(response.beneficioDestino);
  }

  private upsertBeneficio(beneficio: Beneficio): void {
    const filtered = this.beneficios.filter(({ id }) => id !== beneficio.id);
    this.beneficios = [...filtered, beneficio].sort((a, b) => a.id - b.id);
  }

  private showSuccess(message: string): void {
    this.feedback = { type: 'success', message };
  }

  private showError(message: string): void {
    this.feedback = { type: 'error', message };
  }
}
