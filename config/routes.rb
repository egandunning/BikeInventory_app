Rails.application.routes.draw do
  get 'users/new'

  get 'welcome/index'

  root 'welcome#index'

  resources :articles

  resources :users

  resources :bicycles do
  	resources :rentals
  end

  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
