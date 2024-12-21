using Microsoft.AspNetCore.Mvc;
using System.Security.Claims;
using System.Linq; // Pour les méthodes d'extension sur ClaimsPrincipal

[ApiController]
[Route("api/cart")]
public class CartController : ControllerBase
{
    private readonly OrderService _orderService;

    // Correction ici : Paramètre du constructeur doit commencer par une minuscule
    public CartController(OrderService orderService)
    {
        _orderService = orderService;
    }
    
[HttpGet]
public async Task<IActionResult> GetOrder([FromHeader(Name = "X-User-ID")] int? userIdHeader)
{
    int userId;

    // Récupérer l'ID utilisateur depuis l'en-tête ou les revendications JWT
    if (userIdHeader.HasValue && userIdHeader > 0)
    {
        userId = userIdHeader.Value;
    }
    else
    {
        var userIdString = User.FindFirstValue(ClaimTypes.NameIdentifier);
        if (!int.TryParse(userIdString, out userId))
        {
            return Unauthorized("Invalid user ID.");
        }
    }

    // Récupérer le panier associé à l'utilisateur
    var order = await _orderService.GetOrderAsync(userId);

    if (order == null)
    {
        return NotFound("No order found for the user.");
    }

    return Ok(order);
}


    [HttpPost("add-item")]
    public async Task<IActionResult> AddItemToCart([FromBody] OrderItem orderItem, [FromHeader(Name = "X-User-ID")] int userId)
    {
        // Vérifier que l'ID de l'utilisateur est présent et valide dans l'en-tête
        if (userId <= 0)
        {
            return Unauthorized("User ID is missing or invalid.");
        }

        // Log pour vérifier que l'ID de l'utilisateur est bien reçu
        Console.WriteLine($"ID de l'utilisateur: {userId}");

        await _orderService.AddItemToOrderAsync(userId, orderItem);

        // Retourner l'état actuel de la commande pour vérification
        var order = await _orderService.GetOrderAsync(userId);
        return Ok(order);
    }

    [HttpDelete("remove-item/{itemId}")]
    public async Task<IActionResult> RemoveItem(int itemId)
    {
        // Récupérer l'ID utilisateur et le convertir en int
        var userIdString = User.FindFirstValue(ClaimTypes.NameIdentifier);
        if (int.TryParse(userIdString, out int userId))
        {
            await _orderService.RemoveItemFromOrderAsync(userId, itemId);
            return Ok();
        }
        else
        {
            return Unauthorized("Invalid user ID.");
        }
    }
}
