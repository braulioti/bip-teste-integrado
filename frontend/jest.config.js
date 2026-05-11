const { createCjsPreset } = require('jest-preset-angular/presets');

module.exports = {
  ...createCjsPreset(),
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/setup-jest.ts'],
  testMatch: ['<rootDir>/src/**/*.spec.ts'],
  collectCoverageFrom: ['src/**/*.ts', '!src/main.ts'],
  coverageDirectory: '<rootDir>/coverage',
  coverageReporters: ['lcov', 'cobertura', 'text-summary'],
  reporters: [
    'default',
    [
      'jest-junit',
      {
        outputDirectory: 'reports/jest',
        outputName: 'junit.xml',
      },
    ],
  ],
};
