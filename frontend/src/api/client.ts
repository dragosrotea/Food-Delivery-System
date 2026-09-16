const API_URL = import.meta.env.VITE_API_URL ?? "http://localhost:8080";

type ApiErrorBody = {
  message?: string;
};

export class ApiError extends Error {
  constructor(
    message: string,
    public readonly status: number,
  ) {
    super(message);
    this.name = "ApiError";
  }
}

export async function apiRequest<T>(path: string): Promise<T> {
  const response = await fetch(`${API_URL}${path}`, {
    headers: {
      Accept: "application/json",
    },
  });

  if (!response.ok) {
    let body: ApiErrorBody | undefined;

    try {
      body = (await response.json()) as ApiErrorBody;
    } catch {
      body = undefined;
    }

    throw new ApiError(
      body?.message ?? "The request could not be completed.",
      response.status,
    );
  }

  return (await response.json()) as T;
}
