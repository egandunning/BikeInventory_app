class RentalsController < ApplicationController
	
	def index
		@rentals = Rental.all
	end
	
	def show
		@rental = Rental.find(params[:id])
	end
	
	def new
		@rental = Rental.new
	end
	
	def edit
		@rental = Rental.find(params[:id])
	end
	
	def create
		@bicycle = Bicycle.find(params[:bicycle_id])
		@rental = @bicycle.rentals.create(rental_params)
		redirect_to bicycle_path(@bicycle)
	end
	
	def update
		@rental = Rental.find(params[:id])
		
		if @rental.update(rental_params)
			redirect_to @rental
		else
			render 'edit'
		end
	end
	
	def destroy
		@rental = Rental.find(params[:id])
		@rental.destroy
		
		redirect_to articles_path
	end
				
	private
		def rental_params
			params.require(:rental).permit(:renter, :checkedout, :returned, :email, :phone, :locknum, :helmet)
		end
end
