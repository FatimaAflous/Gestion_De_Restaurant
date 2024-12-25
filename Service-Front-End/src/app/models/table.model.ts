  export interface Table {
    id: number;
    nom: string;
    capacite: number;
    creneau: {
      id: number;
      debut: string;
      fin: string;
    }; // Inclut les détails du créneau
  }

