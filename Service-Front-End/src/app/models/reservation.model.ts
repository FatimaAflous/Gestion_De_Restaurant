export class Reservation {
    constructor(
      public id: number,
      public userId: number,
      public tableId: number,
      public creneauId: number,
      public statut: string,
      public dateReservation: string
    ) {}
  
    // Méthode pour vérifier si une réservation est confirmée
    isConfirmed(): boolean {
      return this.statut === 'CONFIRMEE';
    }
  }
  